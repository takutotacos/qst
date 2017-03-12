package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sugitatakuto on 2017/02/20.
 */
public class Categories {
    @SerializedName("categories")
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
