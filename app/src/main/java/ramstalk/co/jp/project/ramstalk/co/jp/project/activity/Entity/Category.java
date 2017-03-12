package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sugitatakuto on 2017/02/20.
 */
public class Category {

    private String id;

    @SerializedName("category_name")
    private String categoryName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
