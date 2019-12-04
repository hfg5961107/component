package com.easymi.component.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.easymi.component.R;
import com.easymi.component.adapter.PlaceAdapter;
import com.easymi.component.base.RxBaseActivity;
import com.easymi.component.entity.EmLoc;
import com.easymi.component.utils.EmUtil;
import com.easymi.component.utils.StringUtils;
import com.easymi.component.widget.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: hufeng
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class PlaceActivity extends RxBaseActivity {
    public static final int CITY_CODE = 0X11;

    EditText editText;
    SwipeRecyclerView recyclerView;
    TextView cityName;

    private PlaceAdapter adapter;

    private PoiSearch search;

    private int page = 0;

    private String keyWord = "";
    private String city = "";

    @Override
    public boolean isEnableSwipe() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_place;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

        editText = findViewById(R.id.edit_search);
        recyclerView = findViewById(R.id.recyclerView);
        cityName = findViewById(R.id.city_name);

        String hint = getIntent().getStringExtra("hint");
        if (StringUtils.isNotBlank(hint)) {
            editText.setHint(hint);
        }

        EmLoc loc = EmUtil.getLastLoc();
        if (null != loc) {
            cityName.setText(loc.city);
            city = loc.city;
        }

        cityName.setOnClickListener(v -> {
            Intent intent = new Intent(PlaceActivity.this, CityActivity.class);
            startActivityForResult(intent, CITY_CODE);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new PlaceAdapter(this);
        adapter.setItemClickListener(item -> {
            Intent intent = new Intent();
            intent.putExtra("poiItem", item);
            setResult(RESULT_OK, intent);
            finish();
        });
        recyclerView.setAdapter(adapter);

        recyclerView.setOnLoadListener(new SwipeRecyclerView.OnLoadListener() {
            @Override
            public void onRefresh() {
                page = 0;
                searchNearBy(keyWord, city);
            }

            @Override
            public void onLoadMore() {
                page++;
                searchNearBy(keyWord, city);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                keyWord = editable.toString();
                page = 0;
                searchNearBy(keyWord, city);
            }
        });

//        searchNearBy("", city);
    }

    private List<PoiItem> items = new ArrayList<>();

    private void searchNearBy(String keyWord, String city) {

        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageNum(page);
        query.setPageSize(10);
        search = new PoiSearch(this, query);

        EmLoc loc = EmUtil.getLastLoc();
        if (StringUtils.isBlank(keyWord) && city.equals(loc.city)) {
            search.setBound(new PoiSearch.SearchBound(new LatLonPoint(loc.latitude,
                    loc.longitude), 0));
        } else if (!city.equals(loc.city) && StringUtils.isBlank(keyWord)) {
            keyWord = getString(R.string.key_word);
            query = new PoiSearch.Query(keyWord, "", city);
            search.setQuery(query);
        }

        search.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                recyclerView.complete();
                if (null != poiResult && poiResult.getPois() != null) {
                    if (page == 0) {
                        items.clear();
                        items.addAll(poiResult.getPois());
                    } else {
                        items.addAll(poiResult.getPois());
                    }
                    adapter.setPoiList(items);
                    if (poiResult.getPageCount() - 1 > page) {
                        recyclerView.setLoadMoreEnable(true);
                    } else {
                        recyclerView.setLoadMoreEnable(false);
                    }
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        search.searchPOIAsyn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CITY_CODE) {
                page = 0;
                city = data.getStringExtra("city");
                cityName.setText(city);
                searchNearBy(keyWord, city);
            }
        }
    }

    public void cancel(View view) {
        finish();
    }
}
