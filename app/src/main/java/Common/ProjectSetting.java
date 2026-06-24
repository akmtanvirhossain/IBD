package Common;

/**
 * Created by TanvirHossain on 19/07/2016.
 */
public class ProjectSetting {
    //public static String ProjectName    = "mnhlisting";
    public static String Namespace      = "http://203.190.254.42/";

    public static String ServerURL      = "https://103.174.189.131/";

    // ---------------------------------------------------------------------------------------------
    //public static String ProjectName    = "ibdsurveillance";
    //public static String APIName       = "ibdsurveillance";


    public static String ProjectName    = "ibdsurveillance_temp";
    public static String APIName       = "ibdsurveillance_temp";

    // ---------------------------------------------------------------------------------------------
    public static String VersionDate    = "16062026"; //Format: DDMMYYYY

    // ---------------------------------------------------------------------------------------------
    public static String apiName         = ProjectName.toLowerCase();
    public static String NewVersionName  = ProjectName.toLowerCase() +"_update";
    public static String DatabaseFolder  = ProjectName.toUpperCase() +"DB";
    public static String DatabaseName    = ProjectName.toUpperCase() +"Database.db";
    public static String zipDatabaseName = ProjectName.toUpperCase() +"Database.zip";
    public static String DBSecurityPass  = "a";
    public static String Organization    = "ICDDR,B";
    public static String Soap_Address    = ProjectSetting.ServerURL + "/"+ ProjectSetting.apiName +"/datasync.asmx";
    public static String UpdatedSystem   = ProjectSetting.ServerURL + "/"+ ProjectSetting.apiName +"/Update/"+ ProjectSetting.NewVersionName +".txt";
}
