package customer.oncloud_service.handlers;

import java.util.HashMap;
 import java.util.Map;

 import org.springframework.stereotype.Component;

//  import com.sap.cds.services.cds.CdsCreateEventContext;
 import com.sap.cds.services.cds.CdsReadEventContext;
 import com.sap.cds.services.cds.CdsService;
 import com.sap.cds.services.handler.EventHandler;
 import com.sap.cds.services.handler.annotations.On;
 import com.sap.cds.services.handler.annotations.ServiceName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 @Component
 @ServiceName("AdminService")
 public class AdminService implements EventHandler {
     private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
  
     private Map<Object, Map<String, Object>> products = new HashMap<>();

    //  @On(event = CdsService.EVENT_CREATE, entity = "AdminService.Tododetail")
    //  public void onCreate(CdsCreateEventContext context) {
    //      context.getCqn().entries().forEach(e -> products.put(e.get("ID"), e));
    //      context.setResult(context.getCqn().entries());
    //  }

     @On(event = CdsService.EVENT_READ, entity = "AdminService.Tododetail")
     public void onRead(CdsReadEventContext context) {
         logger.info("start to read");

         context.setResult(products.values());
     }

 }
    
