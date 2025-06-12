package com.hychen11.common.valid;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();

    // 初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        // 合法的值
        int[] values = constraintAnnotation.values();
        // 将合法值全部放到set中，便于查找是否存在
        for (int value : values) {
            set.add(value);
        }
    }

    // 判断是否校验成功
    @Override
    /*
     *
     * @params value 提交过来需要检验的值
     * @params context 校验的上下文环境信息
     * @return
     */
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(value);
    }
}