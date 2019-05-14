package com.ventoray.projectmanager.ui.main_activity

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
import com.ventoray.projectmanager.BaseActivity
import com.ventoray.projectmanager.R
import com.ventoray.projectmanager.data.repo.ProjectRepository
import com.ventoray.projectmanager.data.util.DbUtil
import com.ventoray.projectmanager.util.FileManager
import com.ventoray.projectmanager.util.Files.USER_OBJECT_FILE
import com.ventoray.projectmanager.util.MessageUtil
import com.ventoray.projectmanager.util.PreferenceUtilK
import com.ventoray.projectmanager.web.APIv1
import com.ventoray.projectmanager.data.datamodel.User
import com.ventoray.projectmanager.ui.PreSignInActivity
import com.ventoray.projectmanager.ui.util.ScrimController
import com.ventoray.projectmanager.util.EventBusUtil
import com.ventoray.projectmanager.web.VolleySingleton
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    //TODO replace this
    private var user: User? =
        User()

    private lateinit var emailTextView: TextView
    private lateinit var userNameTextView: TextView

    private lateinit var projectsViewPager: ViewPager
    private lateinit var projectsTabLayout: TabLayout

    private lateinit var searchBar: SearchView

    private lateinit var bottomSheetView: CardView
    private lateinit var bottomSheet: BottomSheetBehavior<CardView>

    private lateinit var scrim: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        scrim.setOnClickListener{view ->
            scrim?.visibility = View.GONE
            bottomSheet?.state = STATE_HIDDEN
        }

        searchBar.setOnQueryTextListener(QueryTextListener())

        setUpBottomSheet()
        getUserData()
        setUpTabLayout()
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

    class QueryTextListener: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            EventBus.getDefault().post(EventBusUtil.SearchEvent(query))
            if (query.isNullOrEmpty()) return false
            return true
        }

        override fun onQueryTextChange(query: String?): Boolean {
            EventBus.getDefault().post(EventBusUtil.SearchEvent(query))
            if (query.isNullOrEmpty()) return false
            return true
        }
    }

    private fun setUpTabLayout(): Unit {
        projectsTabLayout = findViewById<TabLayout>(R.id.projectsTabLayout)
        projectsViewPager = findViewById<ViewPager>(R.id.projectsViewPager)
        projectsTabLayout.setupWithViewPager(projectsViewPager)
        projectsTabLayout.tabMode = TabLayout.MODE_FIXED
        projectsViewPager.adapter =
            ProjectsPageAdapter(supportFragmentManager, this)
        //TODO viewPager.setPageTransformer

    }

    /**
     * Checks if user profile exists on file, if not, retrieve from API
     */
    private fun getUserData() {
        val obj: Any? = FileManager.readObjectFromFile(applicationContext, USER_OBJECT_FILE)

        user = obj as User?

        if (user == null || user?.id == 0) {
            getUserFromWeb { user ->
                user?.let {
                    //Begin download of projects
                    ProjectRepository(this).downLoadProjects(user.id)
                }
            }
        } else {
            setNavHeaderData()
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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


    private fun logout(): Unit {
        //TODO remove user info e.g. database
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
                DbUtil(applicationContext).removeAllUserData { message, success ->
                    Log.d("MainActivity", message)
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

    private fun getUserFromWeb(onComplete: (User?)->Unit): Unit {
        val token: String? = PreferenceUtilK.getClientPasswordToken(applicationContext)
        if (token == null || token.isEmpty()) {
            val intent: Intent = Intent()
            intent.setClass(this, PreSignInActivity::class.java)
            startActivity(intent)
        }

        val stringRequest = object : StringRequest(
            Request.Method.GET, APIv1.URL_USER,
            Response.Listener<String> { response ->
                response?.let {
                    Log.i("User", response)
                    user = Gson().fromJson(response, User::class.java)
                    FileManager.writeObjectToFile(this, user, USER_OBJECT_FILE)

                    //TODO control flow for error
                    setNavHeaderData()
                    onComplete(user)
                }
            },
            Response.ErrorListener {
                it?.let { Log.e("RetreiveUser", it.message ?: "null message") }
                Toast.makeText(this, R.string.retrieve_user_failed, Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return HashMap<String, String>().apply { put(APIv1.HEADER_AUTHORIZATION, "Bearer $token") }
            }
        }
        VolleySingleton.getInstance(applicationContext).addToRequestQueue(stringRequest)
    }

    private fun setNavHeaderData() {
        userNameTextView?.setText(user?.username)
        emailTextView?.setText(user?.email)
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
