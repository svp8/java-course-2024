/*
 * This file is generated by jOOQ.
 */

package scrapper.domain.jooq.tables.records;

import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import scrapper.domain.jooq.tables.Answer;

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
public class AnswerRecord extends UpdatableRecordImpl<AnswerRecord> implements Record3<Long, OffsetDateTime, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>ANSWER.ID</code>.
     */
    public void setId(@NotNull Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>ANSWER.ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>ANSWER.CREATED_AT</code>.
     */
    public void setCreatedAt(@Nullable OffsetDateTime value) {
        set(1, value);
    }

    /**
     * Getter for <code>ANSWER.CREATED_AT</code>.
     */
    @Nullable
    public OffsetDateTime getCreatedAt() {
        return (OffsetDateTime) get(1);
    }

    /**
     * Setter for <code>ANSWER.LINK_ID</code>.
     */
    public void setLinkId(@NotNull Integer value) {
        set(2, value);
    }

    /**
     * Getter for <code>ANSWER.LINK_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getLinkId() {
        return (Integer) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row3<Long, OffsetDateTime, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row3<Long, OffsetDateTime, Integer> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Answer.ANSWER.ID;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field2() {
        return Answer.ANSWER.CREATED_AT;
    }

    @Override
    @NotNull
    public Field<Integer> field3() {
        return Answer.ANSWER.LINK_ID;
    }

    @Override
    @NotNull
    public Long component1() {
        return getId();
    }

    @Override
    @Nullable
    public OffsetDateTime component2() {
        return getCreatedAt();
    }

    @Override
    @NotNull
    public Integer component3() {
        return getLinkId();
    }

    @Override
    @NotNull
    public Long value1() {
        return getId();
    }

    @Override
    @Nullable
    public OffsetDateTime value2() {
        return getCreatedAt();
    }

    @Override
    @NotNull
    public Integer value3() {
        return getLinkId();
    }

    @Override
    @NotNull
    public AnswerRecord value1(@NotNull Long value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public AnswerRecord value2(@Nullable OffsetDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    @NotNull
    public AnswerRecord value3(@NotNull Integer value) {
        setLinkId(value);
        return this;
    }

    @Override
    @NotNull
    public AnswerRecord values(@NotNull Long value1, @Nullable OffsetDateTime value2, @NotNull Integer value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AnswerRecord
     */
    public AnswerRecord() {
        super(Answer.ANSWER);
    }

    /**
     * Create a detached, initialised AnswerRecord
     */
    @ConstructorProperties({"id", "createdAt", "linkId"})
    public AnswerRecord(@NotNull Long id, @Nullable OffsetDateTime createdAt, @NotNull Integer linkId) {
        super(Answer.ANSWER);

        setId(id);
        setCreatedAt(createdAt);
        setLinkId(linkId);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised AnswerRecord
     */
    public AnswerRecord(scrapper.domain.jooq.tables.pojos.Answer value) {
        super(Answer.ANSWER);

        if (value != null) {
            setId(value.getId());
            setCreatedAt(value.getCreatedAt());
            setLinkId(value.getLinkId());
            resetChangedOnNotNull();
        }
    }
}
