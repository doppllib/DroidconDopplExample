package co.touchlab.droidconandroid.shared.network.sessionize;


import java.util.List;

public class Category
{
    private int                id;
    private String             name;
    private List<CategoryItem> categoryItems;
    private int                sort;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<CategoryItem> getCategoryItems()
    {
        return categoryItems;
    }

    public void setCategoryItems(List<CategoryItem> categoryItems)
    {
        this.categoryItems = categoryItems;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    static class CategoryItem
    {
        public int    id;
        public String name;
    }
}
