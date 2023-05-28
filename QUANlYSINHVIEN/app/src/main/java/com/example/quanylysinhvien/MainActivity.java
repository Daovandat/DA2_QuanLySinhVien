package com.example.quanylysinhvien;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quanylysinhvien.adapter.SinhVienAdapter;
import com.example.quanylysinhvien.dao.SinhVienDao;
import com.example.quanylysinhvien.model.TaikhoanMatKhau;
import com.example.quanylysinhvien.loginandregisteractivity.LoginActivity;
import com.example.quanylysinhvien.model.SinhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText edtSearch;
    public static ArrayList<SinhVien> ds = new ArrayList<>();
    ArrayList<SinhVien> timKiem = new ArrayList<>();

    SinhVienAdapter sinhVienAdapter;
    FloatingActionButton fbadd;
    FloatingActionButton fab;
    FloatingActionButton fbHome;
    FloatingActionButton fabDangXuat;
    SinhVienDao sinhVienDao;
    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtSearch = findViewById(R.id.edtsearch);
        listView = findViewById(R.id.listSV);
        fbadd = findViewById(R.id.fbsearch);
        fab = findViewById(R.id.fab);
        fbHome = findViewById(R.id.fbHome);
        fabDangXuat = findViewById(R.id.fbDangXuat);
        //
        sinhVienDao = new SinhVienDao(MainActivity.this);
        if (DanhSachLopActivity.xetList == true) {
            ds = DanhSachLopActivity.svlistDuocLoc;
        } else {
            ds = sinhVienDao.getALL();
        }
        timKiem = sinhVienDao.getALL();
        sinhVienAdapter = new SinhVienAdapter(MainActivity.this, ds);
        listView.setAdapter(sinhVienAdapter);
        //
        // thêm sinh viên
        fbadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThemSinhVienActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ani_intent, R.anim.ani_intenexit);
            }
        });
        //
        listView.setTextFilterEnabled(true);
        //Thêm điểm
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               SinhVien sinhVien = ds.get(position);
               Intent intent = new Intent(MainActivity.this,ThongTinDiem.class);
               Bundle bundle = new Bundle();
               bundle.putSerializable("SINHVIEN",sinhVien);
               intent.putExtras(bundle);
               startActivity(intent);
            }
        });
        //tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    sinhVienAdapter.resetData();
                } else {
                    sinhVienAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //
        // đăng xuất
        fabDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
        // về trang chủ
        fbHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManagerActivity.class));
            }
        });
        // menu
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    openMenu();


                } else {
                    closeMenu();
                }
            }
        });
        //
    }

    private void openMenu() {
        isOpen = true;
        fbHome.animate().translationY(-getResources().getDimension(R.dimen.stan_60));
        fbadd.animate().translationY(-getResources().getDimension(R.dimen.stan_105));
        fabDangXuat.animate().translationY(-getResources().getDimension(R.dimen.stan_155));
    }

    private void closeMenu() {
        isOpen = false;
        fbHome.animate().translationY(0);
        fbadd.animate().translationY(0);
        fabDangXuat.animate().translationY(0);
    }

    @Override
    protected void onResume() {

        if (DanhSachLopActivity.xetList == true) {
            ds = DanhSachLopActivity.svlistDuocLoc;
        } else {
            ds.clear();
            ds.addAll(sinhVienDao.getALL());
        }

        sinhVienAdapter.notifyDataSetChanged();
        super.onResume();


    }
}
