package custom.frame.ui.activity;

import android.view.View;
import android.widget.FrameLayout;

import custom.frame.R;

public abstract class BaseStatusActivity extends BaseActivity
{
    private FrameLayout frameLayout;

    @Override
    public void setContentView(View view)
    {
        super.setContentView(R.layout.act_status_bar);
        frameLayout = (FrameLayout)findViewById(R.id.satusbar_layout_content);
        frameLayout.addView(view);
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(R.layout.act_status_bar);
        frameLayout = (FrameLayout)findViewById(R.id.satusbar_layout_content);
        frameLayout.addView(View.inflate(this, layoutResID, null));
    }
}
