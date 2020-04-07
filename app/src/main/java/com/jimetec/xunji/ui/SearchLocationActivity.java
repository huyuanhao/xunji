package com.jimetec.xunji.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.common.baseview.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;
import com.jimetec.xunji.R;
import com.jimetec.xunji.adapter.PoiAdapter;
import com.jimetec.xunji.bean.LocationWarnBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchLocationActivity extends BaseActivity {


    @BindView(R.id.ivTitleLeft)
    ImageView mIvTitleLeft;
    @BindView(R.id.rlTitleLeft)
    RelativeLayout mRlTitleLeft;
    @BindView(R.id.etSearch)
    EditText mEtSearch;
    @BindView(R.id.lv)
    ListView mLv;
    @BindView(R.id.view_empty)
    FrameLayout mViewEmpty;
    @BindView(R.id.emptyClick)
    TextView mEmptyClick;
    private PoiSearch.Query mQuery;
    private PoiAdapter mPoiAdapter;

    String keyWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        ButterKnife.bind(this);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
        mPoiAdapter = new PoiAdapter(this);
        mLv.setAdapter(mPoiAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideInput(mEtSearch);
                PoiItem poiItem = mPoiAdapter.getData().get(position);
                Intent intent = new Intent(SearchLocationActivity.this, ConfirmLocationActivity.class);
                intent.putExtra(ConfirmLocationActivity.TAG, poiItem);
                startActivityForResult(intent, 1);

            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = mEtSearch.getText().toString();
                if (TextUtils.isEmpty(key) || key.length() < 2) {
                    mPoiAdapter.setData(null);
                } else {
                    keyWord = key;
                    queryWord(key);
                }
            }
        });
        mLv.setEmptyView(mViewEmpty);
    }


    public void queryWord(final String key) {
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mQuery = new PoiSearch.Query(key, "", "");
        mQuery.setPageSize(30);// 设置每页最多返回多少条poiitem
        mQuery.setPageNum(1);// 设置查第一页
        PoiSearch poiSearch = new PoiSearch(this, mQuery);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult result, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS && keyWord.equalsIgnoreCase(key)) {
                    if (result != null && result.getQuery() != null) {// 搜索poi的结果
                        if (result.getQuery().equals(mQuery)) {// 是否是同一条
                            List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                            mPoiAdapter.setData(poiItems);
                        }else {
                            mEmptyClick.setText("未找到位置，输多一点地点名称试试？");

                        }
                    } else {
                        mEmptyClick.setText("未找到位置，输多一点地点名称试试？");
//                        ToastUtil.show(SearchLocationActivity.this,
//                                R.string.no_result);
                    }
                } else {

                    mEmptyClick.setText("未找到位置，输多一点地点名称试试？");

//                    ToastUtil.showShort("查询失败");
//                    ToastUtil.showerror(this, rCode);
                }


            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }


//        Intent intent = new Intent( );
//        intent.putExtra(LocationWarnBean.TAG,locationWarnBean);
//        setResult(Activity.RESULT_OK,intent);
//        finish();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            LocationWarnBean bean = (LocationWarnBean) data.getSerializableExtra(LocationWarnBean.TAG);
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }

    @OnClick(R.id.rlTitleLeft)
    public void onViewClicked() {
        finish();
    }

    @Override
    public String getEventMode() {
        return "搜索位置";
    }
}
