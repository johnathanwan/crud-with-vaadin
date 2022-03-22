package com.example.crudwithvaadin

import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.*
import com.vaadin.flow.spring.annotation.SpringComponent
import com.vaadin.flow.spring.annotation.UIScope

@SpringComponent
@UIScope
class CustomerEditor(val repository: CustomerRepository) : VerticalLayout(), KeyNotifier {

    /**
     * The currently edited customer
     */
    private var customer: Customer? = null

    /**
     * Fields to edit properties in Customer entity
     */
    private val firstName = TextField("First name")
    private val lastName = TextField("Last name")

    /**
     * Action buttons
     */
    private val save = Button("Save", VaadinIcon.CHECK.create())
    private val cancel = Button("Cancel")
    private val delete = Button("Delete", VaadinIcon.TRASH.create())
    private val actions = HorizontalLayout(save, cancel, delete)

    private val binder = Binder(Customer::class.java)
    private var changeHandler: ChangeHandler? = null


    init {
        this.add(firstName, lastName, actions)

        /**
         * bind using naming convention
         */
        this.let {
            binder.bindInstanceFields(it)
        }
        //binder.bindInstanceFields(this)

        /**
         * Configure and style components
         */
        isSpacing = true

        save.element.themeList.add("primary")
        delete.element.themeList.add("error")

        this.addKeyPressListener(Key.ENTER, { save() })

        /**
         * wire action buttons to save, delete and rest
         */
        save.addClickListener { save() }
        delete.addClickListener { delete() }
        cancel.addClickListener { editCustomer(customer) }
        isVisible = false
    }

    fun delete() {
        repository.delete(customer!!)
        changeHandler!!.onChange()
    }

    fun save() {
        repository.save(customer!!)
        changeHandler!!.onChange()

    }

    interface ChangeHandler {
        fun onChange()
    }


    fun editCustomer(c: Customer?) {
        if (c == null) {
            isVisible = false
            return
        }
        val persisted = (c.id != null)
        customer = if (persisted) {
            /**
             * Find fresh entity for editing
             */
            repository.findById(c.id!!).get()
        } else {
            c
        }
        cancel.isVisible = persisted

        /**
         *  Bind customer properties to similarly named fields
         *  Could also use annotation or "manual binding" or programmatically
         *  moving value from fields to entities before saving
         */
        binder.bean = customer
        isVisible = true

        firstName.focus()
    }

    @Suppress("unused")
    fun setChangeHandler(h: ChangeHandler) {
        /**
         *  ChangeHandler is notified when either save or delete is clicked
         */
        changeHandler = h
    }

}