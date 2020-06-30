package github.ztc.rpcEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum RpcResponseCode {

    SUCCESS(200,"调用方法成功"),
    FAIL(500,"调用错误"),
    NOT_FOUND_CLASS(501,"找不到指定类"),
    NOT_FOUND_METHOD(502,"找不到指定方法");
    
    private final int code;
    private final String msg;
}
