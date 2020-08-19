package visitors;

import generated.SimpleLangBaseVisitor;
import generated.SimpleLangParser;
import langInterface.Type;
import langInterface.Value;
import utils.TypeResolver;

public class ValueExpressionVisitor extends SimpleLangBaseVisitor<Value> {

    @Override
    public Value visitValue(SimpleLangParser.ValueContext ctx) {
        String value = ctx.getText();
        Type type = TypeResolver.getFromValue(ctx);
        return new Value(type, value);
    }
}
