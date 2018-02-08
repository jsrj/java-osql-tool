public class Statement {
    /*  NOTE:

        Called whenever it is determined that a column has been changed in a way
        that will allow it to safely be altered without error or data loss.
        This class will generate the necessary Oracle SQL statement to effect
        that change.

        Step 1 - Instantiate new Statement object using column/table object as reference.
        Step 2 - Invoke the generate() method, with the type of statement required.
    */

    // Statement properties
    private String targetTable;      // <--
    private String targetColumnName; // <--
    private String operationType;    // <--

    // Column-Level properties
    private String columnType;       // <--
    private String columnAttribute;  // <--
    private int    columnSize;       // <--

    // Column-Level flags
    private String index;           // <--
    private String unique;          // <--
    private String nullable;        // <--
    private String primary_key;     // <--
    private String foreign_key;     // <--
    private String defaultVal;      // <--

    private boolean hasDefault;     // <--

    // Getters
    public String  GetTargetTable()     { return  this.targetTable;      }
    public String  GetTargetColumnName(){ return  this.targetColumnName; }
    public String  GetOperationType()   { return  this.operationType;    }
    public String  GetColumnType()      { return  this.columnType;       }
    public String  GetColumnAttribute() { return  this.columnAttribute;  }
    public int     GetColumnSize()      { return  this.columnSize;       }
    public String  GetIndexState()      { return  this.index;            }
    public String  GetUniqueState()     { return  this.unique;           }
    public String  GetNullableState()   { return  this.nullable;         }
    public String  GetPrimaryKeyState() { return  this.primary_key;      }
    public String  GetForeignKeyState() { return  this.foreign_key;      }
    public String  GetDefaultVal()      { return  this.defaultVal;       }

    // Setters
    private void setTargetTable     (String  targetTable     ) {this.targetTable     =targetTable;      }
    private void setOperationType   (String  operationType   ) {this.operationType   =operationType;    }
    private void setTargetColumnName(String  targetColumnName) {this.targetColumnName=targetColumnName; }
    private void setColumnType      (String  columnType      ) {this.columnType      =columnType;       }
    private void setColumnAttribute (String  columnAttribute ) {this.columnAttribute =columnAttribute;  }
    private void setColumnSize      (int     columnSize      ) {this.columnSize      =columnSize;       }
    private void setIndex           (String  index           ) {this.index           =index;            }
    private void setUnique          (String  unique          ) {this.unique          =unique;           }
    private void setNullable        (String  nullable        ) {this.nullable        =nullable;         }
    private void setPrimary_key     (String  primary_key     ) {this.primary_key     =primary_key;      }
    private void setForeign_key     (String  foreign_key     ) {this.foreign_key     =foreign_key;      }
    private void setHasDefault      (boolean hasDefault      ) {this.hasDefault      =hasDefault;       }
    private void setDefaultVal      (String defaultVal       ) {this.defaultVal = (this.hasDefault)? "DEFAULT '"+defaultVal+"'" : ""; }

    // Constructor
    public Statement(
            String TargetTable,
            String OperationType,
            ColObj TemplateObject
    )
    {
        this.setTargetTable      ( TargetTable                );
        this.setOperationType    ( OperationType              );
        this.setTargetColumnName (TemplateObject.getCol_name());
        this.setColumnType       (TemplateObject.getCol_type());
        this.setColumnAttribute  (TemplateObject.getCol_attr());
        this.setColumnSize       (TemplateObject.getCol_size());
        this.setHasDefault       (TemplateObject.hasDefault() );
        this.setDefaultVal       (TemplateObject.getDefault() );

        // These will have to be updated to account for specific syntax of each flag.
        this.setIndex            (TemplateObject.isIndex()      ? " INDEX..."       : ""          );
        this.setUnique           (TemplateObject.isUnique()     ? " UNIQUE"         : ""          );
        this.setNullable         (TemplateObject.isNullable()   ? ""                : " NOT NULL" );
        this.setPrimary_key      (TemplateObject.isPrimaryKey() ? " PRIMARY_KEY..." : ""          );
        this.setForeign_key      (TemplateObject.isForeignKey() ? " FOREIGN_KEY..." : ""          );
    }

    // Statement Generation Method
    public String generate() {
        /* NOTE:

            Use pipe-case to pass in arguments to this method so as to avoid
            confusion with object and variable naming conventions.
         */

        // Statement Templates
        String additional = String.format(
                "%s%s%s%s%s;",
                this.GetIndexState(),
                this.GetUniqueState(),
                this.GetNullableState(),
                this.GetPrimaryKeyState(),
                this.GetForeignKeyState()
        );

        String alterColumn          = String.format(
                "ALTER  %1$s %n"+
                "MODIFY %2$s %3$s(%4$s %5$s) %6$s"+
                additional,                 // <-- Additional Flags and Arguments
                this.GetTargetTable(),      // <-- %1$s : Table Name
                this.GetTargetColumnName(), // <-- %2$s : Column Name
                this.GetColumnType(),       // <-- %3$s : Column Type
                this.GetColumnSize(),       // <-- %4$s : Column Size
                this.GetColumnAttribute(),  // <-- %5%s : Column Attribute
                this.GetDefaultVal()        // <-- %6$s : Default Value if any
        );

        String addColumn            = String.format(
                "ALTER %1$s %n"+
                "ADD   %2$s %3$s(%4$s %5$s) %6$s"+
                additional,                 // <-- Additional Flags and Arguments
                this.GetTargetTable(),      // <-- %1$s : Table Name
                this.GetTargetColumnName(), // <-- %2$s : Column Name
                this.GetColumnType(),       // <-- %3$s : Column Type
                this.GetColumnSize(),       // <-- %4$s : Column Size
                this.GetColumnAttribute(),  // <-- %5%s : Column Attribute
                this.GetDefaultVal()        // <-- %6$s : Default Value if any
        );
        String dropColumn           = String.format(
                "ALTER %1$s %n"+
                "DROP  %2$s",               // <-- Additional Flags and Arguments
                this.GetTargetTable(),      // <-- %1$s : Table Name
                this.GetTargetColumnName()  // <-- %2$s : Column Name
        );
        String dropIndexFlag        = String.format(
                ""
        );
        String addIndexFlag         = String.format(
                ""
        );
        String dropUniqueFlag       = String.format(
                ""
        );
        String addUniqueFlag        = String.format(
                ""
        );
        String dropNotNullFlag      = String.format(
                ""
        );
        String addNotNullFlag       = String.format(
                ""
        );
        String dropPKFlag           = String.format(
                ""
        );
        String addPKFlag            = String.format(
                ""
        );
        String dropFKFlag           = String.format(
                ""
        );
        String addFKFlag            = String.format(
                ""
        );


        switch (this.operationType.toLowerCase()) {

            case "add-column":
                return addColumn;

            case "drop-column":
                return dropColumn;

            case "alter-column":
                return alterColumn;

            case "drop-index-flag":
                return dropIndexFlag;

            case "add-index-flag":
                return addIndexFlag;

            case "drop-unique-flag":
                return dropUniqueFlag;

            case "add-unique-flag":
                return addUniqueFlag;

            case "drop-not-null-flag":
                return dropNotNullFlag;

            case "add-not-null-flag":
                return addNotNullFlag;

            case "drop-pk-flag":
                return dropPKFlag;

            case "add-pk-flag":
                return addPKFlag;

            case "drop-fk-flag":
                return dropFKFlag;

            case "add-fk-flag":
                return addFKFlag;

            default:
                break;
        }

        return "";
    }

}
