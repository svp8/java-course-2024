/*
 * This file is generated by jOOQ.
 */
package scrapper.domain.jooq.tables;


import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
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
import scrapper.domain.jooq.tables.records.CommentRecord;


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
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Comment extends TableImpl<CommentRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>COMMENT</code>
     */
    public static final Comment COMMENT = new Comment();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<CommentRecord> getRecordType() {
        return CommentRecord.class;
    }

    /**
     * The column <code>COMMENT.ID</code>.
     */
    public final TableField<CommentRecord, Long> ID = createField(DSL.name("ID"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>COMMENT.CREATION_DATE</code>.
     */
    public final TableField<CommentRecord, OffsetDateTime> CREATION_DATE = createField(DSL.name("CREATION_DATE"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>COMMENT.LINK_ID</code>.
     */
    public final TableField<CommentRecord, Integer> LINK_ID = createField(DSL.name("LINK_ID"), SQLDataType.INTEGER.nullable(false), this, "");

    private Comment(Name alias, Table<CommentRecord> aliased) {
        this(alias, aliased, null);
    }

    private Comment(Name alias, Table<CommentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>COMMENT</code> table reference
     */
    public Comment(String alias) {
        this(DSL.name(alias), COMMENT);
    }

    /**
     * Create an aliased <code>COMMENT</code> table reference
     */
    public Comment(Name alias) {
        this(alias, COMMENT);
    }

    /**
     * Create a <code>COMMENT</code> table reference
     */
    public Comment() {
        this(DSL.name("COMMENT"), null);
    }

    public <O extends Record> Comment(Table<O> child, ForeignKey<O, CommentRecord> key) {
        super(child, key, COMMENT);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public UniqueKey<CommentRecord> getPrimaryKey() {
        return Keys.COMMENT_PKEY;
    }

    @Override
    @NotNull
    public List<ForeignKey<CommentRecord, ?>> getReferences() {
        return Arrays.asList(Keys.CONSTRAINT_6);
    }

    private transient Link _link;

    /**
     * Get the implicit join path to the <code>PUBLIC.LINK</code> table.
     */
    public Link link() {
        if (_link == null)
            _link = new Link(this, Keys.CONSTRAINT_6);

        return _link;
    }

    @Override
    @NotNull
    public Comment as(String alias) {
        return new Comment(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public Comment as(Name alias) {
        return new Comment(alias, this);
    }

    @Override
    @NotNull
    public Comment as(Table<?> alias) {
        return new Comment(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Comment rename(String name) {
        return new Comment(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Comment rename(Name name) {
        return new Comment(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Comment rename(Table<?> name) {
        return new Comment(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, OffsetDateTime, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Long, ? super OffsetDateTime, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super Long, ? super OffsetDateTime, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
