package ideallions.sayhello;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends Activity {

    String lang_code, lang_tab_name;

    RecyclerView rv;
    NationAdapter adapter;
    EditText et_search;
    ArrayList<SingleItem> NationInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);

        /* 1. 언어설정 가져와서 언어 테이블 컬럼명 가지고 오기  */
        lang_code = Locale.getDefault().getLanguage();

        databaseAccess.open();
        lang_tab_name = databaseAccess.getLangTableName(lang_code);
        databaseAccess.close();

        /* 2. 언어에 따른 국가명 가져오기  */
        databaseAccess.open();
        NationInfoList = databaseAccess.getNationName(lang_tab_name);
        databaseAccess.close();

        /* 3. 가지고 온 국가정보 테이블로 리사이클러뷰 생성 */
        rv = (RecyclerView) findViewById(R.id.rvNation);
        adapter = new NationAdapter(this, NationInfoList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new SlideInUpAnimator());

        /* 4. 검색어 처리  */
        et_search = (EditText) findViewById(R.id.search);
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = et_search.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }
}