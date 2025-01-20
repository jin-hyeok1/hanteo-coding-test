package kr.personal.jh.todo1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * @param <K> K 자료형은 트리 구조 내 Map 의 key 자료형을 나타냅니다.
 *            추가로 식별자를 제외한 검색 조건의 자료형으로 사용됩니다.
 * @param <T> 구조화 시킬 데이터의 자료형 입니다. AbstractNodeClass 를 상속받은 Class 이어야 합니다.
 */
public class StructuredData<K, T extends StructuredData.AbstractNodeClass<K>> {

    private final NodeInfo<T> nodeInfo;
    private final AbstractNodeClass<K> root;
    private int jsonIndent = 2;

    public void setJsonIndent(int indent) {
        jsonIndent = indent;
    }

    /**
     * 첫 초기화 시 자료의 구조화가 이루어집니다.
     * @param nodeInfo {@code NodeInfo} 를 상속받은 instance
     */
    public StructuredData(NodeInfo<T> nodeInfo) {
        this.nodeInfo = nodeInfo;
        this.root = new AbstractNodeClass<>() {
            @Override
            public JSONObject getSimpleJson() {
                return null;
            }

            @Override
            public K getStructureKey() {
                return null;
            }
        };
        for (int i = 0; i < nodeInfo.getDataList().size(); i++) {
            if (nodeInfo.getRelations().containsKey(i)) {
                int finalI = i;
                nodeInfo.getRelations().get(i)
                        .forEach(parentIdx -> nodeInfo.getDataList().get(parentIdx)
                                .addChild(nodeInfo.getDataList().get(finalI)));
            } else {
                this.root.addChild(nodeInfo.getDataList().get(i));
            }
        }
    }

    /**
     * 식별자를 통해 Data를 검색합니다.
     *
     * @param id 식별자
     * @return {@code T}
     */
    public T findById(int id) {
        if (id >= nodeInfo.getDataList().size()) {
            throw new IndexOutOfBoundsException("id is out of bounds");
        }
        return nodeInfo.getDataList().get(id);
    }

    /**
     * 요구사항 1번 기능
     * @param key Map 의 key로 사용되는 값으로 StructuredData 선언 시 사용된 K class
     * @return 구조화 된 자료
     */
    public AbstractNodeClass<K> findByKey(K key) {
        Queue<AbstractNodeClass<K>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            AbstractNodeClass<K> cur = queue.poll();
            if (cur.children.containsKey(key)) {
                return cur.children.get(key);
            }
            cur.children.values().forEach(queue::offer);
        }

        throw new NoSuchElementException("No such element: " + key);
    }

    /**
     * 구조 전체의 데이터를 {@code JSONObject} 로 변환하여 반환합니다.
     *
     * @return {@code JSONObject}
     */
    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        String childrenName = nodeInfo.getChildrenName();
        JSONArray jsonArray = new JSONArray();
        root.children.values().forEach(child -> jsonArray.put(child.getFullJson()));
        return json.put(childrenName, jsonArray);
    }

    /**
     * 구조 전체의 데이터를 {@code JSONObjectText} 로 변환하여 반환합니다.<br/>
     * default json indent 는 2이며 {@code setJsonIndex(int indent)} 함수를 통해 변경 가능합니다.
     *
     * @return {@code JSONObject}
     */
    public String getJsonText() {
        return getJson().toString(jsonIndent);
    }

    /**
     * 구조 전체의 데이터를 {@code JSONObject} 로 변환하여 반환합니다.<br/>
     * comparator 사용 시 json 내부 값이 parameter 조건에 따라 정렬되어 표기됩니다.
     *
     * @return {@code JSONObject}
     */
    public JSONObject getJson(Comparator<? super AbstractNodeClass<K>> comparator) {
        JSONObject json = new JSONObject();
        String childrenName = nodeInfo.getChildrenName();
        JSONArray jsonArray = new JSONArray();
        root.children.values().stream().sorted(comparator).forEach(child -> jsonArray.put(child.getFullJson(comparator)));
        return json.put(childrenName, jsonArray);
    }

    /**
     * 구조 전체의 데이터를 {@code JSONObjectText} 로 변환하여 반환합니다.<br/>
     * comparator 사용 시 json 내부 값이 parameter 조건에 따라 정렬되어 표기됩니다.<br/>
     * default json indent 는 2이며 {@code setJsonIndex(int indent)} 함수를 통해 변경 가능합니다.
     *
     * @return {@code JSONObject}
     */
    public String getJsonText(Comparator<? super AbstractNodeClass<K>> comparator) {
        return getJson(comparator).toString(jsonIndent);
    }

    /**
     * @param <T>
     */
    public interface NodeInfo<T extends Node> {
        Map<Integer, List<Integer>> getRelations();

        String getChildrenName();

        List<T> getDataList();
    }

    /**
     * 구조화를 위한 인터페이스입니다.<br/>
     * 편리한 구조화를 위해 {@code AbstractNodeClass} 사용을 권장합니다.
     */
    public interface Node {
        /**
         * 구조화 전 Json object 를 출력합니다.<br/>
         * 구조화 이후 해당 값을 기반으로 children 정보가 추가되어 Json 으로 출력됩니다.
         *
         * @return {@code JSONObject}
         */
        JSONObject getSimpleJson();

    }

    /**
     * 1-N 데이터를 트리 자료 구조로 변환 시키기 위한 상속 객체입니다.<br/>
     * 구조화를 위해서 entity class 는 해당 class 를 상속받아야 합니다.
     */
    public static abstract class AbstractNodeClass<T> implements Node {
        Map<T, AbstractNodeClass<T>> children;

        /**
         * Node 내 Map 에 key 값으로 저장 될 값을 반환합니다.
         *
         * @return {@code T} Node 내 childrenMap 에 저장 시 사용되는 key 값
         */
        abstract T getStructureKey();

        /**
         * 트리 자료구조로 변환 이후의 {@code JSONObject} 객체를 출력합니다.
         *
         * @return {@code JsonObject}
         */
        public JSONObject getFullJson() {
            JSONObject json = getSimpleJson();
            if (!children.isEmpty()) {
                JSONArray childrenArray = new JSONArray();
                children.values().forEach(child -> childrenArray.put(child.getFullJson()));
                json.put(this.getClass().getSimpleName().toLowerCase(), childrenArray);
            }
            return json;
        }

        public JSONObject getFullJson(Comparator<? super AbstractNodeClass<T>> comparator) {
            JSONObject json = getSimpleJson();
            if (!children.isEmpty()) {
                JSONArray childrenArray = new JSONArray();
                children.values().stream().sorted(comparator).forEach(child -> childrenArray.put(child.getFullJson()));
                json.put(this.getClass().getSimpleName().toLowerCase(), childrenArray);
            }
            return json;
        }

        /**
         * 트리 자료구조로 변환 이후의 {@code JSONObject} 객체를 {@code String}으로 변환하여 출력합니다.<br/>
         * 요구사항 2번 기능
         * @return {@code String}
         */
        public String getFullJsonText() {
            return getFullJson().toString(2);
        }

        /**
         * 트리 자료구조로 변환 이후의 {@code JSONObject} 객체를 {@code String}으로 변환하여 출력합니다.
         *
         * @return {@code String}
         */
        public String getFullJsonText(Comparator<? super AbstractNodeClass<T>> comparator) {
            return getFullJson(comparator).toString(2);
        }

        /**
         * child 추가 시 사용되는 method 입니다.<br/>
         * 구조화 이후에도 별도의 child node 를 추가 시킬 수 있습니다.
         *
         * @param child 추가시킬 child instance
         */
        public void addChild(AbstractNodeClass<T> child) {
            this.children.put(child.getStructureKey(), child);
        }

        public AbstractNodeClass() {
            children = new HashMap<>();
        }
    }
}
