/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.apache.openaz.xacml.admin.view.components;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.openaz.xacml.admin.jpa.Attribute;
import org.apache.openaz.xacml.admin.jpa.ConstraintValue;
import org.apache.openaz.xacml.admin.util.AdminNotification;
import org.apache.openaz.xacml.api.Identifier;
import org.apache.openaz.xacml.api.XACML3;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class RangeEditorComponent extends CustomComponent {
	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Panel panelTester;
	@AutoGenerated
	private VerticalLayout verticalLayout_2;
	@AutoGenerated
	private Button buttonValidate;
	@AutoGenerated
	private TextField textFieldTestInput;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_2;
	@AutoGenerated
	private TextField textFieldMax;
	@AutoGenerated
	private ComboBox comboBoxMax;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;
	@AutoGenerated
	private TextField textFieldMin;
	@AutoGenerated
	private ComboBox comboBoxMin;
	private static final long serialVersionUID = -1L;
	private static final Log logger	= LogFactory.getLog(RangeEditorComponent.class);
	private final RangeEditorComponent self = this;
	private final Attribute attribute;
	private Identifier datatype;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	public RangeEditorComponent(Attribute attribute, Identifier datatype) {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		//
		// Save our attribute
		//
		this.attribute = attribute;
		this.datatype = datatype;
		//
		// Finish initialization
		//
		this.initializeCombos();	
		this.initializeTextFields();
		this.initializeTest();
		this.setupDatatype(this.datatype);
	}
	
	private void initializeCombos() {
		//
		// Add the 2 possible values into each combo box
		//
		this.comboBoxMin.setNullSelectionAllowed(true);
		this.comboBoxMin.addItem("minInclusive");
		this.comboBoxMin.addItem("minExclusive");		
		this.comboBoxMax.addItem("maxInclusive");
		this.comboBoxMax.addItem("maxExclusive");
		//
		// Find any current values
		//
		for (ConstraintValue value : this.attribute.getConstraintValues()) {
			if (value.getProperty().equals("minInclusive") ||
				value.getProperty().equals("minExclusive")) {
				//
				// If it hasn't been set yet
				//
				if (this.comboBoxMin.getData() == null) {
					//
					// Select the appropriate combo value
					//
					this.comboBoxMin.select(value.getProperty());
					//
					// Save the object
					//
					this.comboBoxMin.setData(value);
					//
					// Setup the text field
					//
					this.textFieldMin.setValue(value.getValue());
				} else {
					//
					// Extra value in there, this shouldn't happen. But this
					// is here just in case
					//
					logger.warn("Extra min value found: " + value.getProperty() + " " + value.getValue());
				}
			} else if (value.getProperty().equals("maxInclusive") ||
					value.getProperty().equals("maxExclusive")) {
				//
				// Check if it hasn't been set yet
				//
				if (this.comboBoxMax.getData() == null) {
					//
					// Select the appropriate combo value
					//
					this.comboBoxMax.select(value.getProperty());
					//
					// Save the object
					//
					this.comboBoxMax.setData(value);
					//
					// Setup the text field
					//
					this.textFieldMax.setValue(value.getValue());
				} else {
					//
					// Extra value in there, this shouldn't happen. But this
					// is here just in case
					//
					logger.warn("Extra max value found: " + value.getProperty() + " " + value.getValue());
				}
			} else {
				logger.warn("Non-range value found: " + value.getProperty() + " " + value.getValue());
			}
		}
		//
		// Were there values?
		//
		if (this.comboBoxMin.getData() == null) {
			//
			// Put a new empty value in there
			//
			ConstraintValue value = new ConstraintValue("minInclusive", null);
			//
			// Associate it with the attribute
			//
			value.setAttribute(this.attribute);
			//
			// Make sure the attribute has it in its list
			//
			this.attribute.addConstraintValue(value);
			//
			// Save it in the combo
			//
			this.comboBoxMin.setData(value);
			//
			// Disable text field
			//
			this.textFieldMin.setEnabled(false);
		}
		if (this.comboBoxMax.getData() == null) {
			//
			// Put a new empty value in there
			//
			ConstraintValue value = new ConstraintValue("maxInclusive", null);
			//
			// Associate it with the attribute
			//
			value.setAttribute(this.attribute);
			//
			// Make sure the attribute has it in its list
			//
			this.attribute.addConstraintValue(value);
			//
			// Save it in the combo
			//
			this.comboBoxMax.setData(value);
			//
			// Disable text field
			//
			this.textFieldMax.setEnabled(false);
		}
		//
		// Respond to combo changes
		//
		this.setupComboText(this.comboBoxMin, this.textFieldMin);
		this.setupComboText(this.comboBoxMax, this.textFieldMax);
	}
	
	private void setupComboText(final ComboBox box, final TextField text) {
		//
		// Respond to combo changes
		//
		box.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//
				// Get the new value
				//
				String property = (String) box.getValue();
				//
				// Get our constraint object
				//
				ConstraintValue value = (ConstraintValue) box.getData();
				//
				// Update our object
				//
				if (property == null) {
					//
					// Clear the text field and disable it
					//
					text.setEnabled(false);
					text.setValue(null);
				} else {
					//
					// Change the property name
					//
					value.setProperty(property);
					//
					// Focus to the text field and enable it
					//
					text.setEnabled(true);
					text.focus();
				}
			}
		});
		
	}
	
	private void initializeTextFields() {
		this.textFieldMin.setNullRepresentation("");
		this.textFieldMin.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//
				// Get our min object
				//
				ConstraintValue value = (ConstraintValue) self.comboBoxMin.getData();
				//
				// Save its new value
				//
				value.setValue(self.textFieldMin.getValue());
			}			
		});
		this.textFieldMax.setNullRepresentation("");
		this.textFieldMax.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				//
				// Get our max object
				//
				ConstraintValue value = (ConstraintValue) self.comboBoxMax.getData();
				//
				// Save its new value
				//
				value.setValue(self.textFieldMax.getValue());
			}
			
		});
	}
	
	private void initializeTest() {
		this.textFieldTestInput.setNullRepresentation("");
		this.textFieldTestInput.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (self.textFieldTestInput.getValue() != null && self.textFieldTestInput.getValue().length() > 0) {
					self.buttonValidate.setEnabled(true);
				} else {
					self.buttonValidate.setEnabled(false);
				}
			}
		});
				
		this.buttonValidate.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//
				// Create our validator and add it into the text
				//
				Validator validator = self.setupValidator(self.datatype);
				if (validator == null) {
					logger.warn("Could not create a validator");
					return;
				}
				self.textFieldTestInput.addValidator(validator);
				//
				// Initiate the validation
				//
				try {
					self.textFieldTestInput.validate();
					//
					// If we get here, then it validated!
					//
					AdminNotification.info("Success! Value is in range");
				} catch (InvalidValueException e) {
					AdminNotification.warn("Failed, Value is NOT in range");
				}
				//
				// Remove the validator
				//
				self.textFieldTestInput.removeValidator(validator);
			}			
		});
	}
	
	private Validator setupValidator(Identifier id) {
		if (logger.isTraceEnabled()) {
			logger.trace("setupValidator: " + id);
		}
		//
		// Get our min/max objects
		//
		ConstraintValue min = (ConstraintValue) self.comboBoxMin.getData();
		ConstraintValue max = (ConstraintValue) self.comboBoxMax.getData();
		Object minObject = self.comboBoxMin.getConvertedValue();
		Object maxObject = self.comboBoxMax.getConvertedValue();
		logger.debug("Converted values: " + minObject + " " + maxObject);
		//
		// Get our min/max values
		//
		String minValue = self.textFieldMin.getValue();
		String maxValue = self.textFieldMax.getValue();
		//
		// What is our datatype?
		//
		if (id.equals(XACML3.ID_DATATYPE_INTEGER)) {
			Integer minimum = null;
			Integer maximum = null;
			boolean minInclusive = true;
			boolean maxInclusive = true;
			if (min.getProperty() != null) {
				if (minValue != null && minValue.length() > 0) {
					minimum = Integer.parseInt(minValue);
				}
				if (min.getProperty().equals("minInclusive")) {
					minInclusive = true;
				} else if (min.getProperty().equals("minExclusive")) {
					minInclusive = false;
				}
			}
			if (max.getProperty() != null) {
				if (maxValue != null && maxValue.length() > 0) {
					maximum = Integer.parseInt(maxValue);
				}
				if (max.getProperty().equals("maxInclusive")) {
					maxInclusive = true;
				} else if (max.getProperty().equals("maxExclusive")) {
					maxInclusive = false;
				}
			}
			IntegerRangeValidator validator = new IntegerRangeValidator("The value is NOT within the range", minimum, maximum);
			validator.setMinValueIncluded(minInclusive);
			validator.setMaxValueIncluded(maxInclusive);
			return validator;
		}
		if (id.equals(XACML3.ID_DATATYPE_DOUBLE)) {
			Double minimum = null;
			Double maximum = null;
			boolean minInclusive = true;
			boolean maxInclusive = true;
			if (min.getProperty() != null) {
				if (minValue != null && minValue.length() > 0) {
					minimum = Double.parseDouble(minValue);
				}
				if (min.getProperty().equals("minInclusive")) {
					minInclusive = true;
				} else if (min.getProperty().equals("minExclusive")) {
					minInclusive = false;
				}
			}
			if (max.getProperty() != null) {
				if (maxValue != null && maxValue.length() > 0) {
					maximum = Double.parseDouble(maxValue);
				}
				if (max.getProperty().equals("maxInclusive")) {
					maxInclusive = true;
				} else if (max.getProperty().equals("maxExclusive")) {
					maxInclusive = false;
				}
			}
			DoubleRangeValidator validator = new DoubleRangeValidator("The value is NOT within the range", minimum, maximum);
			validator.setMinValueIncluded(minInclusive);
			validator.setMaxValueIncluded(maxInclusive);
			return validator;
		}
		
		return null;
	}
	
	public void	setupDatatype(Identifier datatype) {
		if (logger.isTraceEnabled()) {
			logger.trace("setupDatatype: " + datatype);
		}
		this.datatype = datatype;
		if (datatype.equals(XACML3.ID_DATATYPE_INTEGER)) {
			this.textFieldMin.setConverter(Integer.class);
			this.textFieldMax.setConverter(Integer.class);
			this.textFieldTestInput.setConverter(Integer.class);
			return;
		}
		if (datatype.equals(XACML3.ID_DATATYPE_DOUBLE)) {
			this.textFieldMin.setConverter(Double.class);
			this.textFieldMax.setConverter(Double.class);
			this.textFieldTestInput.setConverter(Double.class);
			return;
		}
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("-1px");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		// top-level component properties
		setWidth("-1px");
		setHeight("-1px");
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.addComponent(horizontalLayout_1);
		mainLayout.setExpandRatio(horizontalLayout_1, 1.0f);
		
		// horizontalLayout_2
		horizontalLayout_2 = buildHorizontalLayout_2();
		mainLayout.addComponent(horizontalLayout_2);
		mainLayout.setExpandRatio(horizontalLayout_2, 1.0f);
		
		// panelTester
		panelTester = buildPanelTester();
		mainLayout.addComponent(panelTester);
		mainLayout.setExpandRatio(panelTester, 1.0f);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);
		
		// comboBoxMin
		comboBoxMin = new ComboBox();
		comboBoxMin.setCaption("Minimum Type");
		comboBoxMin.setImmediate(true);
		comboBoxMin.setDescription("Select the type for the minimum.");
		comboBoxMin.setWidth("-1px");
		comboBoxMin.setHeight("-1px");
		horizontalLayout_1.addComponent(comboBoxMin);
		
		// textFieldMin
		textFieldMin = new TextField();
		textFieldMin.setCaption("Minimum Value");
		textFieldMin.setImmediate(true);
		textFieldMin.setDescription("Enter a value for the minimum.");
		textFieldMin.setWidth("-1px");
		textFieldMin.setHeight("-1px");
		textFieldMin.setInvalidAllowed(false);
		textFieldMin.setInputPrompt("eg. 1");
		horizontalLayout_1.addComponent(textFieldMin);
		horizontalLayout_1
				.setComponentAlignment(textFieldMin, new Alignment(9));
		
		return horizontalLayout_1;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_2() {
		// common part: create layout
		horizontalLayout_2 = new HorizontalLayout();
		horizontalLayout_2.setImmediate(false);
		horizontalLayout_2.setWidth("-1px");
		horizontalLayout_2.setHeight("-1px");
		horizontalLayout_2.setMargin(false);
		horizontalLayout_2.setSpacing(true);
		
		// comboBoxMax
		comboBoxMax = new ComboBox();
		comboBoxMax.setCaption("Maximum Type");
		comboBoxMax.setImmediate(true);
		comboBoxMax.setDescription("Select the type for the maximum.");
		comboBoxMax.setWidth("-1px");
		comboBoxMax.setHeight("-1px");
		horizontalLayout_2.addComponent(comboBoxMax);
		
		// textFieldMax
		textFieldMax = new TextField();
		textFieldMax.setCaption("Maximum Value");
		textFieldMax.setImmediate(true);
		textFieldMax.setDescription("Enter a value for the maxmum.");
		textFieldMax.setWidth("-1px");
		textFieldMax.setHeight("-1px");
		textFieldMax.setInvalidAllowed(false);
		textFieldMax.setInputPrompt("eg. 100");
		horizontalLayout_2.addComponent(textFieldMax);
		
		return horizontalLayout_2;
	}

	@AutoGenerated
	private Panel buildPanelTester() {
		// common part: create layout
		panelTester = new Panel();
		panelTester.setCaption("Test Range Values");
		panelTester.setImmediate(true);
		panelTester.setWidth("-1px");
		panelTester.setHeight("-1px");
		
		// verticalLayout_2
		verticalLayout_2 = buildVerticalLayout_2();
		panelTester.setContent(verticalLayout_2);
		
		return panelTester;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_2() {
		// common part: create layout
		verticalLayout_2 = new VerticalLayout();
		verticalLayout_2.setImmediate(false);
		verticalLayout_2.setWidth("100.0%");
		verticalLayout_2.setHeight("100.0%");
		verticalLayout_2.setMargin(false);
		verticalLayout_2.setSpacing(true);
		
		// textFieldTestInput
		textFieldTestInput = new TextField();
		textFieldTestInput.setCaption("Value");
		textFieldTestInput.setImmediate(true);
		textFieldTestInput.setDescription("Enter a value to test against.");
		textFieldTestInput.setWidth("-1px");
		textFieldTestInput.setHeight("-1px");
		textFieldTestInput.setInputPrompt("eg. 50");
		verticalLayout_2.addComponent(textFieldTestInput);
		
		// buttonValidate
		buttonValidate = new Button();
		buttonValidate.setCaption("Test");
		buttonValidate.setImmediate(true);
		buttonValidate
				.setDescription("Click to test if value is within the range.");
		buttonValidate.setWidth("-1px");
		buttonValidate.setHeight("-1px");
		verticalLayout_2.addComponent(buttonValidate);
		verticalLayout_2.setComponentAlignment(buttonValidate,
				new Alignment(48));
		
		return verticalLayout_2;
	}
	
}