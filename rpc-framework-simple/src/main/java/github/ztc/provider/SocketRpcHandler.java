package github.ztc.provider;

import github.ztc.dto.RpcRequest;
import github.ztc.dto.RpcResponse;
import github.ztc.exception.RpcException;
import github.ztc.rpcEnum.RpcErrorMessageEnum;
import github.ztc.rpcEnum.RpcResponseCode;

import javax.xml.ws.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * @title: SocketRpcHandler
 * @description: 实际执行方法并且返回结果的类
 * @author: ztc
 * @date: 2020/07/01 04:02 16:02
 * @version: V1.0
*/
public class SocketRpcHandler {

    private static ServiceProvider serviceProvider =new ServiceProviderImpl();

    public Object handle(RpcRequest request){
        Object service = SocketRpcHandler.serviceProvider.getServiceProvider(request.getInterfaceName());
        return invokeTargetMethod(service,request);
    }

    private Object invokeTargetMethod(Object service,RpcRequest request){
        Object result =null;
        try {
            Method method =service.getClass().getMethod(request.getMethodName(),request.getParamTypes());
            if(method==null){
                throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE);
            }
             result= method.invoke(service, request.getParameters());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return request;
    }
}
