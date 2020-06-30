package github.ztc.dto;

import github.ztc.rpcEnum.RpcResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = 6498351746454550788L;

    //UUID
    private String requestId;

    //响应码
    private Integer code;

    //响应消息
    private String message;

    //响应数据
    private T data;

    public RpcResponse<T> success(T data,String requestId){
//        RpcResponse build = RpcResponse.builder().requestId(requestId).data(data).build();
        RpcResponse<T> resp = new RpcResponse<>();
        resp.setData(data);
        resp.setRequestId(requestId);

        resp.setCode(RpcResponseCode.SUCCESS.getCode());
        resp.setMessage(RpcResponseCode.SUCCESS.getMsg());
        return resp;
    }

    public RpcResponse<T> fail(RpcResponseCode rpcResponseCode){
        RpcResponse<T> resp =new RpcResponse<>();
        resp.setMessage(rpcResponseCode.getMsg());
        resp.setCode(rpcResponseCode.getCode());
        return resp;
    }


}
