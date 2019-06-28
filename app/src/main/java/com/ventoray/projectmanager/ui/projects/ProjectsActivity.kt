package com.ventoray.projectmanager.ui.projects

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.*
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.Channel
import com.pusher.client.channel.SubscriptionEventListener
import com.ventoray.projectmanager.BaseActivity
import com.ventoray.projectmanager.BuildConfig
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.data.repo.ProjectRepository
import com.ventoray.projectmanager.data.util.DbUtil
import com.ventoray.projectmanager.util.FileManager
import com.ventoray.projectmanager.util.Files.USER_OBJECT_FILE
import com.ventoray.projectmanager.util.MessageUtil
import com.ventoray.projectmanager.util.PreferenceUtilK
import com.ventoray.projectmanager.api.APIv1
import com.ventoray.projectmanager.ui.PreSignInActivity
import com.ventoray.projectmanager.ui.common.ScrimController
import com.ventoray.projectmanager.api.VolleySingleton
import com.ventoray.projectmanager.data.datamodel.Project
import com.ventoray.projectmanager.data.datamodel.User
import com.ventoray.projectmanager.data.repo.Resource
import com.ventoray.projectmanager.data.repo.UserRepository
import com.ventoray.projectmanager.di.ViewModelFactory
import com.ventoray.projectmanager.ui.common.UserViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

class ProjectsActivity : BaseActivity(), HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var emailTextView: TextView
    private lateinit var userNameTextView: TextView

    private lateinit var projectsViewPager: ViewPager
    private lateinit var projectsTabLayout: TabLayout

    private lateinit var searchBar: SearchView

    private lateinit var bottomSheetView: CardView
    private lateinit var bottomSheet: BottomSheetBehavior<CardView>

    private lateinit var scrim: View

    /**
     * Both of these are suspect... the whole scheme for downloading data will likely change
     */
    @Inject lateinit var projectRepository: ProjectRepository
    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var dBUtil: DbUtil
    @Inject lateinit var androidFragInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var userViewModel: UserViewModel

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return androidFragInjector

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this) //Call before super!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        emailTextView = navView.getHeaderView(0).findViewById(R.id.emailTextView)
        userNameTextView = navView.getHeaderView(0).findViewById(R.id.userNameTextView)
        searchBar = findViewById(R.id.searchBar)
        scrim = findViewById(R.id.scrim)

        scrim.setOnClickListener{ view ->
            scrim.visibility = View.GONE
            bottomSheet.state = STATE_HIDDEN
        }

        searchBar.setOnQueryTextListener(QueryTextListener())

        userViewModel.setTokenData(PreferenceUtilK.getClientPasswordToken(this))
        val string = PreferenceUtilK.getClientPasswordToken(this)

        string?.let {
            val liveData: LiveData<Resource<User>>? = userViewModel.getUser(string)
            liveData?.observe(this, Observer { user ->
                if (user?.data != null) {
                    userNameTextView.text = user.data.username
                    emailTextView.text = user.data.email
                }
            })
        }

        setUpBottomSheet()
        setUpTabLayout()
        testPusher()
    }


    /**
     * This is just a proof of concept method for Pusher.
     *
     *   It basically shows that pusher can update the UI in realtime with the server DB
     */
    private fun testPusher() {
        var options = PusherOptions();
        options.setCluster("mt1");
        val pusher = Pusher(BuildConfig.PUSHER_APP_KEY, options);
        val channel: Channel = pusher.subscribe("projects-channel");
        channel.bind("App\\Events\\ProjectCreated", SubscriptionEventListener() { channelName, eventName, data ->
            val jsonObject = JSONObject(data)
            val projectJson = jsonObject.getJSONObject("project")
            val gson: Gson = GsonBuilder().setLenient().create()
            val project: Project? = gson.fromJson(projectJson.toString(), Project::class.java)

            CoroutineScope(Dispatchers.IO).launch {
                project?.let {
                    projectRepository.insert(project)
                }
            }
        })

        pusher.connect();
    }

    private fun setUpBottomSheet() {
        bottomSheetView = findViewById(R.id.bottomSheetView)
        bottomSheet = BottomSheetBehavior.from(bottomSheetView).apply {
            state = STATE_COLLAPSED
            isHideable = true
            peekHeight = 0
            setBottomSheetCallback(ScrimController(scrim))
        }
    }

    private fun setUpTabLayout() {
        projectsTabLayout = findViewById<TabLayout>(R.id.projectsTabLayout)
        projectsViewPager = findViewById<ViewPager>(R.id.projectsViewPager)
        projectsTabLayout.setupWithViewPager(projectsViewPager)
        projectsTabLayout.tabMode = TabLayout.MODE_FIXED
        projectsViewPager.adapter =
            ProjectsPageAdapter(supportFragmentManager, this)

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            finishAffinity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                bottomSheet.state = STATE_EXPANDED
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.logout -> {
                logout()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    //TODO remove this from Activity class
    private fun logout(): Unit {
        val token: String? = PreferenceUtilK.getClientPasswordToken(applicationContext)
        if (token == null || token.isEmpty()) {
            val intent: Intent = Intent()
            intent.setClass(this, PreSignInActivity::class.java)
            startActivity(intent)
        }
        val stringRequest = object : StringRequest(
            Request.Method.GET, APIv1.URL_LOGOUT,
            Response.Listener<String> { response ->
                response?.let {
                    Log.i("Logout", response)
                    Toast.makeText(this, R.string.logged_out, Toast.LENGTH_SHORT).show()
                }

                //delete user data from database
                dBUtil.removeAllUserData { message, success ->
                    Log.d("ProjectsActivity", message)
                }

                //remove token
                PreferenceUtilK.removeTokenPreferences(applicationContext)
                //set user to not logged in
                PreferenceUtilK.setUserNotSignedIn(applicationContext)

                //Delete user object file
                if (FileManager.deleteFile(applicationContext, USER_OBJECT_FILE)) {
                    Log.i("LogOut", "Deleted user profile from device")
                } else {
                    Log.e("LogOut", "Could not remove user profile")
                }

                val intent: Intent = Intent()
                intent.setClass(this, PreSignInActivity::class.java)
                startActivity(intent)
            },
            Response.ErrorListener {
                it?.let { Log.e("Logout", it.message ?: "null message") }
                Toast.makeText(this, R.string.logout_failed, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply { put(APIv1.HEADER_AUTHORIZATION, "Bearer $token") }
            }
        }
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }


    /**
     *@param connected - true if network state changed
     */
    override fun onNetworkStateChange(connected: Boolean) {
        super.onNetworkStateChange(connected)
        if (!connected) {
            MessageUtil.makeToast(this, "Lost Connection")
        } else {
            MessageUtil.makeToast(this, "Connectivity Restored")

        }
    }

}
