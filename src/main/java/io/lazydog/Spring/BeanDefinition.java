package io.lazydog.Spring;

import lombok.Data;

/**
 * BeanDefinition
 *
 * @author lazydog
 * @date 2022/3/16
 */
@Data
public class BeanDefinition {
    private Class type;
    private String scope;
}
