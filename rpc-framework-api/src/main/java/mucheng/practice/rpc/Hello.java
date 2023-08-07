package mucheng.practice.rpc;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author mucheng
 * @date 2023/08/07 17:01:00
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Hello implements Serializable {
    private static final long serialVersionUID = 4980745032165289993L;

    /**
     * 消息
     */
    private String message;
    /**
     * 描述
     */
    private String description;
}
