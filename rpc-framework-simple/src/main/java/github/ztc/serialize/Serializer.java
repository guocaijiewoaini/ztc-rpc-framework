package github.ztc.serialize;

public interface Serializer {
    /**
        序列化对象
     */
    byte[] serialize(Object o);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] bytes,Class<T> clazz);
}
