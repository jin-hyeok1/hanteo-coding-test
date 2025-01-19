package kr.personal.jh.todo1;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDatabase implements StructuredData.NodeInfo<CategoryDatabase.Category> {

    private List<Category> categories;
    //key: parent_idx, value: child_id
    private final Map<Integer, Integer> relations;

    public CategoryDatabase() {
        this.categories = new ArrayList<>();
        this.relations = new HashMap<>();
    }

    public void defineRelation(int parent, int child) {
        if (parent == child) {
            throw new IllegalArgumentException("parent is the same as the child");
        }
        if (parent < 0 || child < 0) {
            throw new IllegalArgumentException("parent and child must be greater than 0");
        }
        if (parent >= categories.size() || child >= categories.size()) {
            throw new IllegalArgumentException("parent and child must not be greater than " + categories.size());
        }
        if (relations.containsKey(parent)) {
            System.out.printf("%d parent change child %d to %d\n", parent, relations.get(parent), child);
        }
        this.relations.put(parent, child);
    }

    public Category save(Category category) {
        Category e = category.setId(categories.size());
        categories.add(e);
        return e;
    }

    @Override
    public Map<Integer, Integer> getRelations() {
        return relations;
    }

    @Override
    public String getChildrenName() {
        return "category";
    }

    @Override
    public List<Category> getChildren() {
        return categories;
    }

    public static class Category extends StructuredData.AbstractNodeClass<String> {
        private int id;
        private final String name;
        private boolean hasAnnouncement;
        private boolean hasAnonymous;

        public Category(String name, boolean hasAnnouncement, boolean hasAnonymous) {
            this.name = name;
            this.hasAnnouncement = hasAnnouncement;
            this.hasAnonymous = hasAnonymous;
        }

        private Category setId(int id) {
            this.id = id;
            return this;
        }

        public void setHasAnnouncement(boolean hasAnnouncement) {
            this.hasAnnouncement = hasAnnouncement;
        }

        public void setHasAnonymous(boolean hasAnonymous) {
            this.hasAnonymous = hasAnonymous;
        }

        @Override
        public JSONObject toJSON() {
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
