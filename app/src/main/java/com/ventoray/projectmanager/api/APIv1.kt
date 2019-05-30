package com.ventoray.projectmanager.api

import com.ventoray.projectmanager.BuildConfig

class APIv1 {



    companion object Constants {

        const val PROTOCOL = "http://"
        const val API_V1 = "/api/v1"
        const val API_CURRENT = API_V1

        const val URL_BASE = PROTOCOL + BuildConfig.TEST_SERVER_IP_ADDRESS

        /**
         * URL Endpoints
         */
        const val URL_TEST = PROTOCOL + BuildConfig.TEST_SERVER_IP_ADDRESS + API_CURRENT + "/test"
        const val URL_TOKEN_REQUEST = PROTOCOL + BuildConfig.TEST_SERVER_IP_ADDRESS + "/oauth/token"
        const val URL_LOGOUT = PROTOCOL + BuildConfig.TEST_SERVER_IP_ADDRESS + API_CURRENT + "/logout"
        const val URL_USER = PROTOCOL + BuildConfig.TEST_SERVER_IP_ADDRESS + API_CURRENT + "/user"
        const val URL_USERS = PROTOCOL + BuildConfig.TEST_SERVER_IP_ADDRESS + API_CURRENT + "/users"
        const val URL_PROJECTS = PROTOCOL + BuildConfig.TEST_SERVER_IP_ADDRESS + API_CURRENT + "/users"

        /**
         * Header Fields
         */
        const val HEADER_AUTHORIZATION = "authorization"

        /**
         * Content types
         */
        const val CONTENT_TYPE_BODY = "application/json; charset=utf-8"

        /**
         * Request Body Params
         */
        const val PARAM_PASSWORD: String = "password"
        const val PARAM_USERNAME: String = "username"
        const val PARAM_CLIENT_ID: String = "client_id"
        const val PARAM_CLIENT_SECRET: String = "client_secret"
        const val PARAM_GRANT_TYPE: String = "grant_type"

        /**
         * Values that will be constant
         */
        const val VALUE_GRANT_TYPE: String = "password"

        /**
         * Response fields
         */
        const val RESPONSE_KEY_ACCESS_TOKEN: String = "access_token"
    }


}