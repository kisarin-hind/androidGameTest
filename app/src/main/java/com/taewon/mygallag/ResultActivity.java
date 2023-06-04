package com.taewon.mygallag;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_dialog);
        init();
    }
    private void init(){//goMainBtn 을 누르면 초기화면으로 이동
        findViewById(R.id.goMainBtn).setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        });
        // 결과창에 점수 표시
        ((TextView)findViewById(R.id.userFinalScoreText)).setText(getIntent().getIntExtra("score",0)+"");
    }
    //결과화면에서 뒤로가기 버튼 사용 막음
    @Override
    public void onBackPressed() {
        ExitDialog exitDialog = new ExitDialog(this);

        exitDialog.show();
    }
}
