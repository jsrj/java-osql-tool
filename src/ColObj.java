

public class ColObj {

    // Common Attributes to all Columns
    private String col_name;
    private String col_type;
    private String col_attr;
    private String defaultVal;
    private int    col_size;

    // Optional/Additional Parameters
    private boolean isIndex      = false;
    private boolean isUnique     = false;
    private boolean isNullable   = true;
    private boolean isPrimaryKey = false;
    private boolean isForeignKey = false;
    private boolean hasDefault   = false;

    // Constructor
    public ColObj(String name,
                  String type,
                  String attr,
                  String defaultVal,
                  int    size,
                  String additional)
    {
        this.setCol_name(name);
        this.setCol_type(type);
        this.setCol_attr(attr);
        this.setDefault (defaultVal);
        this.setCol_size(size);
        if (!defaultVal.equals("")) { this.hasDefault = true; }

        String[] extraParams = additional.split(";");
        for (String param: extraParams) {
            switch (param.toUpperCase()) {
                case "INDEX":
                    this.toggleIndex();
                    break;

                case "UNIQUE":
                    this.toggleUnique();
                    break;

                case "NOTNULL":
                    this.toggleNullable();
                    break;

                case "PRIMARYKEY":
                    this.togglePrimaryKey();
                    break;

                case "FOREIGNKEY":
                    this.toggleForeignKey();
                    break;

                default:
                    break;
            }
        }
    }


    // Getters
    public String  getCol_name()  { return this.col_name;     }
    public String  getCol_type()  { return this.col_type;     }
    public String  getCol_attr()  { return this.col_attr;     }
    public String  getDefault ()  { return (this.hasDefault()) ? this.defaultVal : "";   }
    public int     getCol_size()  { return this.col_size;     }
    public boolean isIndex()      { return this.isIndex;      }
    public boolean isUnique()     { return this.isUnique;     }
    public boolean isNullable()   { return this.isNullable;   }
    public boolean isPrimaryKey() { return this.isPrimaryKey; }
    public boolean isForeignKey() { return this.isForeignKey; }
    public boolean hasDefault()   { return this.hasDefault;   }


    // Setters - W/  Args
    private void setCol_name(String name    ) { this.col_name = name;     }
    private void setCol_type(String col_type) { this.col_type = col_type; }
    private void setCol_attr(String col_attr) { this.col_attr = col_attr; }
    private void setDefault (String defaultV) { this.defaultVal=defaultV; }
    private void setCol_size(int    col_size) { this.col_size = col_size; }
    // Setters - W/O Args
    private void toggleIndex()      { this.isIndex      = !this.isIndex;      }
    private void toggleUnique()     { this.isUnique     = !this.isUnique;     }
    private void toggleNullable()   { this.isNullable   = !this.isNullable;   }
    private void togglePrimaryKey() { this.isPrimaryKey = !this.isPrimaryKey; }
    private void toggleForeignKey() { this.isForeignKey = !this.isForeignKey; }
}
