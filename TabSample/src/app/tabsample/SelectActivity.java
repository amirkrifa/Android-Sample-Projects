package app.tabsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * @author Administrator
 *
 */
public class SelectActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LayoutInflater.from(getParent()).inflate(R.layout.tabstest1, null));
    }
}
