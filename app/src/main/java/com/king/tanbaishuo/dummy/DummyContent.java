package com.king.tanbaishuo.dummy;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 * @author Administrator
 */
public class DummyContent {
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    public static class DummyItem {
        public final String id;  // QQ
        public final String content; //内容
        public final String details;  //对发送者的描述内容

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
