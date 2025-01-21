package kr.personal.jh.todo1;

import org.json.JSONObject;

import java.util.*;

/**
 * Category 를 자료구조인 StructuredData 를 사용하기 위한 {@code class} 입니다.
 */
public class CategoryDatabase implements StructuredData.NodeInfo<CategoryDatabase.Category> {

    private final List<Category> categories;
    //key: child_id, value: parent_idx
    private final Map<Integer, List<Integer>> relations;

    public CategoryDatabase() {
        this.categories = new ArrayList<>();
        this.relations = new HashMap<>();
    }

    public CategoryDatabase(List<Category> categories, Map<Integer, List<Integer>> relations) {
        this.categories = categories;
        this.relations = relations;
    }

    public CategoryDatabase defineRelation(int parent, int child) {
        if (parent == child) {
            throw new IllegalArgumentException("parent is the same as the child");
        }
        if (parent < 0 || child < 0) {
            throw new IllegalArgumentException("parent and child must be greater than 0");
        }
        if (parent >= categories.size() || child >= categories.size()) {
            throw new IllegalArgumentException("parent and child must not be greater than " + categories.size());
        }
        if (relations.containsKey(child)) {
            this.relations.get(child).add(parent);
        } else {
            ArrayList<Integer> parents = new ArrayList<>();
            parents.add(parent);
            this.relations.put(child, parents);
        }
        return this;
    }

    public Category save(Category category) {
        category.setId(categories.size());
        categories.add(category);
        return category;
    }

    public Category[] saveAll(Category... categories) {
        if (categories == null || categories.length == 0) {
            throw new IllegalArgumentException("categories must not be null or empty");
        }
        Arrays.stream(categories).forEach(this::save);
        return categories;
    }

    @Override
    public Map<Integer, List<Integer>> getRelations() {
        return relations;
    }

    @Override
    public String getChildrenName() {
        return "category";
    }

    @Override
    public List<Category> getDataList() {
        return categories;
    }

    public static class Category extends StructuredData.AbstractNodeClass<String> {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private final Category instance;

            private Builder() {
                this.instance = new Category(null);
            }

            public Builder name(String name) {
                instance.name = name;
                return this;
            }

            public Category build() {
                return instance;
            }
        }

        public Category(String name) {
            this.name = name;
        }

        private void setId(int id) {
            this.id = id;
        }

        @Override
        public JSONObject getSimpleJson() {
            return new JSONObject()
                    .put("id", id)
                    .put("name", name);
        }

        @Override
        public String getStructureKey() {
            return name;
        }
    }
}
