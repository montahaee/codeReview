package pipesag.optimizer;

import pipesag.datastructure.Order;
import pipesag.datastructure.CuttingProcess;

public interface OffcutsCalculable {

    public CuttingProcess computeOffcuts(Order order);
}
