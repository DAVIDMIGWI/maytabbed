package com.davyy.mytabbed.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.davyy.mytabbed.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class MenuFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_menu, container, false)
        val client = AsyncHttpClient(true,80,443)
        val progress=root.findViewById(R.id.progress) as ProgressBar
        progress.visibility=View.VISIBLE

        client.get("https://migwi81.pythonanywhere.com/api/posts",null,object :
        JsonHttpResponseHandler()
        {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?,
                response: JSONArray
            ) {
                //super.onSuccess(statusCode, headers, response)
                for( i in 0 until response.length()){
                    val json= response.getJSONObject(i)
                    val firstname=json.optString("firstname").toString()
                    val lastname=json.optString("lastname").toString()
                    val phone=json.optString("phone").toString()
                    val request=json.optString("request").toString()
                    val residence=json.optString("residence").toString()
                    val textview=root.findViewById(R.id.data)as TextView
                    textview.append("$firstname\n $lastname\n $phone\n $request\n $residence")
                    textview.append("\n\n")
                }
                progress.visibility=View.GONE
            }//end onsuccess



        })
        return root
    }


}