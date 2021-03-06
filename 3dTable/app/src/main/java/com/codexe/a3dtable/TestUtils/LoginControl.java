package com.codexe.a3dtable.TestUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModelProvider;

import com.codexe.a3dtable.MainActivity;
import com.codexe.a3dtable.model.User;
import com.codexe.a3dtable.ui.login.LoginViewModel;


public class LoginControl extends AsyncTask<Void, Void, Boolean> {

    private Context context; //Activity baslatmak icin gerekli
    private ProgressBar progressBar;
    private String mail, passwd;
    private User user = null;

    public LoginControl(ProgressBar progressBar, Context context, String mail, String password) {
        this.progressBar = progressBar;
        this.context = context;
        this.mail = mail;
        this.passwd = password;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE); //Evvelce gizli olan pb'yi tekrar gorunur yapiyoruz
    }


    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e("csd", e.getMessage());
        }

        SampleDB sampleDB = SampleDB.getInstance();

        Boolean result = new Boolean(sampleDB.isRegister(mail, passwd));


        if (result)
            user = sampleDB.getUser(mail);
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            ((Activity) context).finish();

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);



            intent.putExtra("userMail", mail);
            intent.putExtra("user",user);

//            Log.v("login_control","UserName: "+user);


            context.startActivity(intent);

        }
    }
}
