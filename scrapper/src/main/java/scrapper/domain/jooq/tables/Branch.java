/*
 * This file is generated by jOOQ.
 */

package scrapper.domain.jooq.tables;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import scrapper.domain.jooq.DefaultSchema;
import scrapper.domain.jooq.Keys;
import scrapper.domain.jooq.tables.records.BranchRecord;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Branch extends TableImpl<BranchRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>BRANCH</code>
     */
    public static final Branch BRANCH = new Branch();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<BranchRecord> getRecordType() {
        return BranchRecord.class;
    }

    /**
     * The column <code>BRANCH.NAME</code>.
     */
    public final TableField<BranchRecord, String> NAME =
        createField(DSL.name("NAME"), SQLDataType.VARCHAR(1000000000).nullable(false), this, "");

    /**
     * The column <code>BRANCH.LINK_ID</code>.
     */
    public final TableField<BranchRecord, Integer> LINK_ID =
        createField(DSL.name("LINK_ID"), SQLDataType.INTEGER.nullable(false), this, "");

    private Branch(Name alias, Table<BranchRecord> aliased) {
        this(alias, aliased, null);
    }

    private Branch(Name alias, Table<BranchRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>BRANCH</code> table reference
     */
    public Branch(String alias) {
        this(DSL.name(alias), BRANCH);
    }

    /**
     * Create an aliased <code>BRANCH</code> table reference
     */
    public Branch(Name alias) {
        this(alias, BRANCH);
    }

    /**
     * Create a <code>BRANCH</code> table reference
     */
    public Branch() {
        this(DSL.name("BRANCH"), null);
    }

    public <O extends Record> Branch(Table<O> child, ForeignKey<O, BranchRecord> key) {
        super(child, key, BRANCH);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<BranchRecord> getPrimaryKey() {
        return Keys.BRANCH_PKEY;
    }

    @Override
    @NotNull
    public List<ForeignKey<BranchRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CONSTRAINT_7);
    }

    private transient Link _link;

    /**
     * Get the implicit join path to the <code>PUBLIC.LINK</code> table.
     */
    public Link link() {
        if (_link == null) {
            _link = new Link(this, Keys.CONSTRAINT_7);
        }

        return _link;
    }

    @Override
    @NotNull
    public Branch as(String alias) {
        return new Branch(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public Branch as(Name alias) {
        return new Branch(alias, this);
    }

    @Override
    @NotNull
    public Branch as(Table<?> alias) {
        return new Branch(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Branch rename(String name) {
        return new Branch(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Branch rename(Name name) {
        return new Branch(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Branch rename(Table<?> name) {
        return new Branch(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row2<String, Integer> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function2<? super String, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function2<? super String, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
