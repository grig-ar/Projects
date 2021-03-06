package utils;

import com.google.common.primitives.Ints;
import generated.SimpleLangParser;
import langInterface.BuiltInType;
import langInterface.ClassType;
import langInterface.Type;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public final class TypeResolver {

    public static Type getFromTypeContext(SimpleLangParser.PrimitiveTypeContext typeContext) {
        if (typeContext == null) {
            return BuiltInType.VOID;
        }
        return getFromTypeName(typeContext.getText());
    }

    private static Type getFromTypeName(String typeName) {
        if (typeName.equals("java.lang.String")) {
            return BuiltInType.STRING;
        }
        Optional<? extends Type> builtInType = getBuiltInType(typeName);
        if (builtInType.isPresent()) {
            return builtInType.get();
        }
        return new ClassType(typeName);
    }

    public static Type getFromValue(SimpleLangParser.ValueContext value) {
        String stringValue = value.getText();
        if (StringUtils.isEmpty(stringValue)) {
            return BuiltInType.VOID;
        }
        if (value.NUMBER() != null) {
            if (Ints.tryParse(stringValue) != null) {
                return BuiltInType.INT;
            } /*else if (Floats.tryParse(stringValue) != null) {
                return BuiltInType.FLOAT;
            } else if (Doubles.tryParse(stringValue) != null) {
                return BuiltInType.DOUBLE;
            }*/
        }
        return BuiltInType.STRING;
    }

    public static Object getValueFromString(String stringValue, Type type) {
        if (TypeChecker.isInt(type)) {
            return Integer.valueOf(stringValue);
        }
//        if (TypeChecker.isFloat(type)) {
//            return Float.valueOf(stringValue);
//        }
//        if (TypeChecker.isDouble(type)) {
//            return Double.valueOf(stringValue);
//        }
//        if (TypeChecker.isBool(type)) {
//            return Boolean.valueOf(stringValue);
//        }
        if (type == BuiltInType.STRING) {
            stringValue = StringUtils.removeStart(stringValue, "\"");
            stringValue = StringUtils.removeEnd(stringValue, "\"");
            return stringValue;
        }
        throw new AssertionError("Objects not yet implemented!");
    }

    private static Optional<BuiltInType> getBuiltInType(String typeName) {
        return Arrays.stream(BuiltInType.values())
                .filter(type -> type.getName().equals(typeName))
                .findFirst();
    }
}