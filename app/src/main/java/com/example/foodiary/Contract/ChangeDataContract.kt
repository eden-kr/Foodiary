package com.example.foodiary.Contract

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.ContactsContract
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

class ChangeDataContract {
    interface View{
        fun getHeight() : Int
        fun getWeight() : Float
        fun getSex() : String
        fun getMaxCal() : Int
        fun setMyProfile(bitmap: Bitmap)
        fun makeToast(messege : String)
        fun makeDialog()
        fun setMyId(id : String)
    }
    interface Presenter{
        fun setData()
        fun attachView(view : View)
        fun detachView()
        fun uploadMyProfile()
        fun getPhotoPermission(context: Context)
        fun geUri(context: Context,uri : Uri)
        fun dipose()
        fun streamToBitmap(context: Context,uri: Uri)
        fun getMyId(context: Context)
    }
}