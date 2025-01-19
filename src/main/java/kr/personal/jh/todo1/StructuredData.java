package kr.personal.jh.todo1;

import org.json.JSONObject;

import java.util.*;

public class StructuredData<K, T extends StructuredData.Node<K>> {

    private final NodeInfo<T> nodeInfo;
    private AbstractNodeClass<K> root;

    public StructuredData(NodeInfo<T> nodeInfo) {
        this.nodeInfo = nodeInfo;
        this.root = new AbstractNodeClass<>() {
            @Override
            public JSONObject toJSON() {
                return new JSONObject();
            }

            @Override
            public String getStructureKey() {
                return null;
            }
        };
    }

    public T findById(int id) {
        if (id >= nodeInfo.getChildren().size()) {
            throw new IndexOutOfBoundsException("id is out of bounds");
        }
        return nodeInfo.getChildren().get(id);
    }

    public AbstractNodeClass<K> findByName(K key) {
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

    public interface NodeInfo<T extends Node> {
        Map<Integer, Integer> getRelations();
        String getChildrenName();
        List<T> getChildren();
    }

    public interface Node<Key> {
        JSONObject toJSON();
        String getStructureKey();
    }

    public static abstract class AbstractNodeClass<T> implements Node<T> {
        Map<T, AbstractNodeClass<T>> children;

        public AbstractNodeClass() {
            children = new HashMap<>();
        }
    }
}
