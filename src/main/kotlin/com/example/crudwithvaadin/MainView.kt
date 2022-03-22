package com.example.crudwithvaadin


import com.example.crudwithvaadin.CustomerEditor.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.*
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.*
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.*
import com.vaadin.flow.router.*

@Suppress("unused")
@Route
class MainView(private val repository: CustomerRepository, private val editor: CustomerEditor) : VerticalLayout() {
    private val grid: Grid<Customer> = Grid(Customer::class.java)
    private val filter = TextField()
    private val addNewBtn = Button("New customer", VaadinIcon.PLUS.create())

    init {
        /**
         * build layout
         */
        val actions = HorizontalLayout(filter, addNewBtn)
        add(actions, grid, editor)

        grid.height = "300px"
        grid.setColumns("id", "firstName", "lastName")
        grid.getColumnByKey("id").setWidth("50px").flexGrow = 0

        filter.placeholder = "Filter by last name"

        /**
         * Hook logic to components
         */

        /**
         * Replace listing with filtered content when user changes filter
         */
        filter.valueChangeMode = ValueChangeMode.EAGER
        filter.addValueChangeListener { listCustomers(it.value) }

        /**
         * Connect selected Customer to editor or hide if none is selected
         */
        grid.asSingleSelect().addValueChangeListener { editor.editCustomer(it.value) }

        /**
         * Instantiate and edit new Customer the new button is clicked
         */
        addNewBtn.addClickListener { editor.editCustomer(Customer("", "")) }

        /**
         * Listen changes made by the editor, refresh data from backend
         */
        editor.setChangeHandler(object : ChangeHandler {
            override fun onChange() {
                editor.isVisible = false
                listCustomers(filter.value)
            }
        })

        /**
         * Initialize listing
         */
        listCustomers(null)
    }

     private fun listCustomers(filterText: String?) {
        if (filterText.isNullOrBlank()) {
            grid.setItems(repository.findAll())
        } else {
            grid.setItems(repository.findByLastNameStartsWithIgnoreCase(filterText))
        }
    }

}