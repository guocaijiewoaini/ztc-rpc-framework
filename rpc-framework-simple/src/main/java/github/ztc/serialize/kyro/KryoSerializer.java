package github.ztc.serialize.kyro;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import github.ztc.dto.RpcRequest;
import github.ztc.dto.RpcResponse;
import github.ztc.exception.SerializeException;
import github.ztc.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Supplier;

public class KryoSerializer implements Serializer {

    private final ThreadLocal<Kryo> kryoThreadLocal =ThreadLocal.withInitial(new Supplier<Kryo>() {
        @Override
        public Kryo get() {
            Kryo kryo =new Kryo();
            kryo.register(RpcResponse.class);
            kryo.register(RpcRequest.class);
            kryo.setReferences(true);//默认true，是否关闭注册行为，关闭之后可能存在序列化问题，一般推荐为true
            kryo.setRegistrationRequired(false);//默认为false,是否关闭循环引用，可以提高性能，一般不推荐true
            return kryo;
        }
    });


    @Override
    public byte[] serialize(Object o) {
        try(ByteArrayOutputStream byteOutputStream =new ByteArrayOutputStream();
            Output output =new Output(byteOutputStream)){
            Kryo kryo =kryoThreadLocal.get();
            kryo.writeObject(output, o);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (IOException e) {
            throw new SerializeException("序列化异常");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try(ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
            Input input =new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            return clazz.cast(o);
        } catch (IOException e) {
            throw new SerializeException("反序列化异常");
        }
    }
}
