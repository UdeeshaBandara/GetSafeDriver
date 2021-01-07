package lk.hd192.getsafedriver.Utils;

import android.database.Observable;
import android.net.Uri;

public interface CapturandroCallback {
    void onImport(int requestCode, Observable<Uri> observable);
}
