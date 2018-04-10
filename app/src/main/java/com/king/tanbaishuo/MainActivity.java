package com.king.tanbaishuo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.tanbaishuo.dummy.DummyContent;
import com.king.tanbaishuo.util.BitMapUtil;
import com.king.tanbaishuo.util.SetImageViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.king.tanbaishuo.dummy.DummyContent.ITEMS;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    //    侧滑栏头部 View全局声明
    private View headerView;
    private ImageView head_topImg;

    String urlLogin = "https://qzone.qq.com/";
    String urlHonest = "https://ti.qq.com/honest-say/my-received.html?_wv=9191&_wwv=132&_qStyle=1&ADTAG=main";
    private WebView webView;
    public static String TAG = "Honest";
    private String cookie;
    private CookieManager cookieManager;
    private MyItemRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private long currnetTime;
    private double touchTime;
    private double waitTime = 2000;
    private TextView textView_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openURL("http://shang.qq" +
                        ".com/wpa/qunwpa?idkey=038d89a94ae973081f240716abb114a9ccb532a32a1a0bff994d460f4760eb7c");
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "长按加入兴趣群", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        initv();
    }

    private void initv() {
        textView_help = (TextView) findViewById(R.id.help);
        textView_help.setMovementMethod(ScrollingMovementMethod.getInstance());
        head_topImg = (ImageView) headerView.findViewById(R.id.imageView);
        head_topImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openURL("http://wpa.qq.com/msgrd?v=3&uin=1776885812&site=qq&menu=yes");
            }
        });
        SetImageViewUtil.setImageToImageView(head_topImg, "http://q2.qlogo" +
                ".cn/headimg_dl?dst_uin=1776885812&spec=100");
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new MyItemRecyclerViewAdapter(ITEMS);
        recyclerView.setAdapter(adapter);
        webView = (WebView) findViewById(R.id.web_view);
        findViewById(R.id.btn_query).setOnClickListener(this);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 8.0; MI 6 Build/OPR1.170623.027; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044006 Mobile Safari/537.36 V1_AND_SQ_7.5.5_806_YYB_D QQ/7.5.5.3460 NetType/4G WebP/0.3.0 Pixel/1080");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    if (webView.getUrl().equals("https://h5.qzone.qq.com/mqzone/index")) {
                        cookieManager = CookieManager.getInstance();
                        cookie = cookieManager.getCookie("https://h5.qzone.qq.com/mqzone/index");
                        Log.d(TAG, "当前cookie" + cookie);
                        Toast.makeText(MainActivity.this.getApplicationContext(),
                                "登陆成功，如未查询成功，请重新登录查询",
                                Toast
                                .LENGTH_LONG).show();
                        webView.loadUrl(urlHonest);
                    }
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView paramAnonymousWebView, final String paramAnonymousString) {
                if (paramAnonymousString.indexOf("cgi-node/honest-say/receive/mine?_client_version=0.0.7&_t=") != -1) {
                    Log.d(TAG, "拿到地址" + paramAnonymousString);
                    cookieManager = CookieManager.getInstance();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL(paramAnonymousString);
                                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                                urlConnection.setConnectTimeout(5000);
                                urlConnection.setReadTimeout(5000);
                                urlConnection.setRequestProperty("cookie", cookie);
                                Log.d(TAG, "加载地址" + paramAnonymousString);
                                int responsecode = urlConnection.getResponseCode();
                                if (responsecode == 200) {
                                    Log.d(TAG, "响应成功，正在读取中");
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                                    String str = "";
                                    if ((str = reader.readLine()) != null) {
                                        Log.d(TAG, "run: 1" + str);
                                        JSONObject jsonObject1 = new JSONObject(str);
                                        if (jsonObject1.getString("code").equals("2333333")) {
                                            Toast.makeText(MainActivity.this, "查询错误，未知错误", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String ss = new Decrypter().Decrypt(str);
                                            ITEMS.clear();
                                            String s[] = ss.split("\n");
                                            for (int i = 0; i < s.length; i++) {
                                                Log.d(TAG, "run: " + i + "--" + s[i]);
                                                String[] s1 = s[i].split("\\|");
                                                ITEMS.add(new DummyContent.DummyItem(s1[0], s1[1], s1[2]));
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    webView.setVisibility(View.GONE);
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    adapter.notifyDataSetChanged();
                                                    Toast.makeText(MainActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                    }
                                } else {
                                    Log.d(TAG, "获取不到网页的源码，服务器响应代码为：" + responsecode);
                                    Toast.makeText(MainActivity.this, "加载失败未知错误", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                Toast.makeText(MainActivity.this, "加载失败未知错误", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                super.onLoadResource(paramAnonymousWebView, paramAnonymousString);
            }

            public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString) {
                try {
                    boolean bool = paramAnonymousString.startsWith("jsbridge://");
                    if (bool) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
                webView.loadUrl(paramAnonymousString);
                return true;
            }
        });
        webView.loadUrl(urlLogin);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query: {
                webView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                webView.loadUrl(urlHonest);
                break;
            }
            case R.id.btn_submit: {
                webView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                webView.loadUrl(urlLogin);
                break;
            }
            case R.id.btn_exit: {
                CookieSyncManager.createInstance(getApplicationContext());
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                CookieSyncManager.getInstance().sync();
                System.exit(0);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openURL("http://wpa.qq.com/msgrd?v=3&uin=1776885812&site=qq&menu=yes");
            return true;
        } else if (id == R.id.action_settings1) {
            openURL("https://github.com/qq576777915/TanBaiShuoV1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openURL(String s) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(s);
        intent.setData(content_url);
        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nar_url1) {
            openURL("http://dkfirst.cn");
        } else if (id == R.id.nar_url2) {
            openURL("http://www.dkingmz.com");
        } else if (id == R.id.nar_url3) {
            openURL("http://www.dkingdg.com");
        } else if (id == R.id.nar_url4) {
            openURL("http://www.dkingds.com");
        } else if (id == R.id.nar_url5) {
            openURL("http://ml.dkfirst.cn");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}