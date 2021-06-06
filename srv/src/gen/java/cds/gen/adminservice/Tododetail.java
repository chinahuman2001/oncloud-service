package cds.gen.adminservice;

import com.sap.cds.CdsData;
import com.sap.cds.Struct;
import com.sap.cds.ql.CdsName;
import java.lang.String;

@CdsName("AdminService.Tododetail")
public interface Tododetail extends CdsData {
  String IDCOUNT = "IDCount";

  @CdsName(IDCOUNT)
  String getIDCount();

  @CdsName(IDCOUNT)
  void setIDCount(String iDCount);

  Tododetail_ ref();

  static Tododetail create() {
    return Struct.create(Tododetail.class);
  }
}
