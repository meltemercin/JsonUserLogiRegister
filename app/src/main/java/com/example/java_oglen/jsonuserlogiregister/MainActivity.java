package com.example.java_oglen.jsonuserlogiregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class MainActivity extends AppCompatActivity {
EditText ad;
    EditText soyad;
    EditText telefon;
    EditText  mail;
    EditText sifre;
    Button kayit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


 //execute yazmayınca tetiklenme olmaz

    }

    public void kayitYap(View v)
    {
        ad=(EditText)findViewById(R.id.a);
        soyad=(EditText)findViewById(R.id.b);
        telefon=(EditText)findViewById(R.id.c);
        mail=(EditText)findViewById(R.id.d);
        sifre=(EditText)findViewById(R.id.e);
        if(ad.getText().toString().equals("")||soyad.getText().toString().equals("")||telefon.getText().toString().equals("")||mail.getText().toString().equals("")||sifre.getText().toString().equals("")){
            Toast.makeText(this, "Eksik Bilgi Girdiniz", Toast.LENGTH_SHORT).show();

        }else{
            String url="http://jsonbulut.com/json/userRegister.php?ref=cb226ff2a31fdd460087fedbb34a6023&" +
                    "userName="+ad.getText().toString() +
                    "&userSurname="+soyad.getText().toString() +
                    "&userPhone="+telefon.getText().toString() +
                    "&userMail="+mail.getText().toString() +
                    "&userPass="+sifre.getText().toString();
            Log.d("URL ", url);
            new jsonData(url,this).execute();


        }

    }
    //Jsondata servisi,jsonla ilgili her aktivitede bu sınıf kullanılabilir

    class jsonData extends AsyncTask<Void,Void,Void> {

        String data = "";
        String url=" ";
        ProgressDialog pro;
        Context cntx;
        public jsonData(String url, Context cntx)
        {
            this.url = url;
            this.cntx=cntx;
            pro=new ProgressDialog(cntx);
            pro.setMessage("İşlem Yapılıyor Lütfen Bekleyiniz");
            pro.show();


        }


        //bu sınıf başlatıldıgın tetiklenecek ilk method
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        //
        @Override
        protected Void doInBackground(Void... params)
        {
            try{

                data= Jsoup.connect(url).ignoreContentType(true).get().body().text();

            }catch (Exception ex){
                Log.e("x","Data Json Hatası");
            }
            return null;
        }
        //

        @Override
        protected void onPostExecute(Void aVoid)
        {

            super.onPostExecute(aVoid);
            //grafiksel özelligi olan işlemler bu bölümde yapılır
            Log.d("Gelen Data:",data);
            try {
//datayı çagırdık önce
                JSONObject obj=new JSONObject(data);
                // bu datadan Adı user olan jsonarrayden 0. elemanın durumu çagırıldı
                //burdan hep 0. eleman dönüyor çünkü 0 elemanlı bir data
                boolean durum=obj.getJSONArray("user").getJSONObject(0).getBoolean("durum");
                String mesaj=obj.getJSONArray("user").getJSONObject(0).getString("mesaj");
                if(durum==true){

                   //kullanıcı kayıt başarılı
                    Toast.makeText(cntx, mesaj, Toast.LENGTH_SHORT).show();
                    String kid=obj.getJSONArray("user").getJSONObject(0).getString("kullaniciId");
                    Log.d("kid",kid);

                }else{
                    Toast.makeText(cntx,mesaj, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //süslü parantezle başlayan Json oble köşeli parantezle başlayan json dizi
            }
            //yükleme ekrenını kapat
            pro.dismiss();

        }


    }
}
