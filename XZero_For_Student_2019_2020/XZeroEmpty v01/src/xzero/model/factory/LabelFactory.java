package xzero.model.factory;

import xzero.model.Label;

/**
 * Фабрика, порождающая метку. Реализует самую простую стратегию
 */
public class LabelFactory {
    public Label createLabel() {
        //!!!
        return new Label();
        //return null;
    }
}
