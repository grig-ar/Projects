package exceptions;

import langInterface.Type;

public class MixedComparisonNotAllowedException extends RuntimeException {
    public MixedComparisonNotAllowedException(Type leftType, Type rightType) {
        super("Comparison between object and primitive is not supported  :" + leftType + "  |  " + rightType);
    }
}
