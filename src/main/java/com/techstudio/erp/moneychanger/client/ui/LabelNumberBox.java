/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple HTML5 number box.
 *
 * @author Nilson
 */
public class LabelNumberBox extends ButtonBase
    implements HasName, HasValue<String>, HasChangeHandlers {

  InputElement inputElem;
  LabelElement labelElem;
  private boolean valueChangeHandlerInitialized;

  private static Element createInputNumber() {
    InputElement ie = Document.get().createTextInputElement();
    ie.setAttribute("type", "number");
    return ie.cast();
  }

  /**
   * Creates an input box with no label.
   */
  public LabelNumberBox() {
    this(createInputNumber());
    setStyleName("field");
  }

  /**
   * Creates an input box with the specified text label.
   *
   * @param label the check box's label
   */
  public LabelNumberBox(String label) {
    this();
    setText(label);
  }

  /**
   * Creates an input box with the specified text label.
   *
   * @param label the input box's label
   * @param asHTML <code>true</code> to treat the specified label as html
   */
  public LabelNumberBox(String label, boolean asHTML) {
    this();
    if (asHTML) {
      setHTML(label);
    } else {
      setText(label);
    }
  }

  protected LabelNumberBox(Element elem) {
    super(DOM.createSpan());
    inputElem = InputElement.as(elem);
    labelElem = Document.get().createLabelElement();

    getElement().appendChild(labelElem);
    getElement().appendChild(inputElem);

    String uid = DOM.createUniqueId();
    inputElem.setPropertyString("id", uid);
    labelElem.setHtmlFor(uid);

    // Accessibility: setting tab index to be 0 by default, ensuring element
    // appears in tab sequence. FocusWidget's setElement method already
    // calls setTabIndex, which is overridden below. However, at the time
    // that this call is made, inputElem has not been created. So, we have
    // to call setTabIndex again, once inputElem has been created.
    setTabIndex(0);
  }

  public HandlerRegistration addValueChangeHandler(
      ValueChangeHandler<String> handler) {
    // Is this the first value change handler? If so, time to add handlers
    if (!valueChangeHandlerInitialized) {
      addChangeHandler(new ChangeHandler() {
        public void onChange(ChangeEvent event) {
          ValueChangeEvent.fire(LabelNumberBox.this, getValue());
        }
      });
      valueChangeHandlerInitialized = true;
    }
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /**
   * Returns the value property of the input element that backs this widget.
   * This is the value that will be associated with the InputLabel name and
   * submitted to the server if a {@link FormPanel} that holds it is submitted.
   * <p>
   * This will probably return the same thing as {@link #getValue}, left here for magic reasons.
   */
  public String getFormValue() {
    return inputElem.getValue();
  }

  @Override
  public String getHTML() {
    return labelElem.getInnerHTML();
  }

  public String getName() {
    return inputElem.getName();
  }

  @Override
  public int getTabIndex() {
    return inputElem.getTabIndex();
  }

  @Override
  public String getText() {
    return labelElem.getInnerText();
  }

  /**
   * Gets the text value of the input element.
   * <p>
   * @return the value of the input box.
   * Will not return null
   */
  public String getValue() {
    if (isAttached()) {
      return inputElem.getValue();
    } else {
      return inputElem.getDefaultValue();
    }
  }

  @Override
  public boolean isEnabled() {
    return !inputElem.isDisabled();
  }

  @Override
  public void setAccessKey(char key) {
    inputElem.setAccessKey("" + key);
  }

  @Override
  public void setEnabled(boolean enabled) {
    inputElem.setDisabled(!enabled);
    if (enabled) {
      removeStyleDependentName("disabled");
    } else {
      addStyleDependentName("disabled");
    }
  }

  @Override
  public void setFocus(boolean focused) {
    if (focused) {
      inputElem.focus();
    } else {
      inputElem.blur();
    }
  }

  /**
   * Set the value property on the input element that backs this widget. This is
   * the value that will be associated with the InputLabel's name and submitted to
   * the server if a {@link FormPanel} that holds it is submitted.
   * <p>
   * Don't confuse this with {@link #setValue}.
   *
   * @param value
   */
  public void setFormValue(String value) {
    inputElem.setAttribute("value", value);
  }

  @Override
  public void setHTML(String html) {
    labelElem.setInnerHTML(html);
  }

  public void setName(String name) {
    inputElem.setName(name);
  }

  @Override
  public void setTabIndex(int index) {
    // Need to guard against call to setTabIndex before inputElem is
    // initialized. This happens because FocusWidget's (a superclass of
    // InputLabel) setElement method calls setTabIndex before inputElem is
    // initialized. See InputLabel's protected constructor for more information.
    if (inputElem != null) {
      inputElem.setTabIndex(index);
    }
  }

  @Override
  public void setText(String text) {
    labelElem.setInnerText(text);
  }

  /**
   * Sets the text in the input box.
   * <p>
   * Note that this <em>does not</em> set the value property of the
   * input element wrapped by this widget. For access to that property, see
   * {@link #setFormValue(String)}
   *
   * @param value the text to set; must not be null
   * @throws IllegalArgumentException if value is null
   */
  public void setValue(String value) {
    setValue(value, false);
  }

  public void setAutoFocus() {
    inputElem.setAttribute("autofocus", "true"); // true is not necessary
  }

  /**
   * Sets the text in the input box, firing {@link ValueChangeEvent} if
   * appropriate.
   * <p>
   * Note that this <em>does not</em> set the value property of the
   * input element wrapped by this widget. For access to that property, see
   * {@link #setFormValue(String)}
   *
   * @param value true the text to set; must not be null
   * @param fireEvents If true, and value has changed, fire a
   *          {@link ValueChangeEvent}
   * @throws IllegalArgumentException if value is null
   */
  public void setValue(String value, boolean fireEvents) {
    if (value == null) {
      throw new IllegalArgumentException("value must not be null");
    }

    String oldValue = getValue();
    inputElem.setValue(value);
    inputElem.setDefaultValue(value);
    if (value.equals(oldValue)) {
      return;
    }
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }

  // Unlike other widgets the InputLabel sinks on its inputElement, not
  // its wrapper
  @Override
  public void sinkEvents(int eventBitsToAdd) {
    if (isOrWasAttached()) {
      Event.sinkEvents(inputElem,
          eventBitsToAdd | Event.getEventsSunk(inputElem));
    } else {
      super.sinkEvents(eventBitsToAdd);
    }
  }


  /**
   * <b>Affected Elements:</b>
   * <ul>
   * <li>-label = label next to the input box.</li>
   * </ul>
   *
   * @see UIObject#onEnsureDebugId(String)
   */
  @Override
  protected void onEnsureDebugId(String baseID) {
    super.onEnsureDebugId(baseID);
    ensureDebugId(labelElem, baseID, "label");
    ensureDebugId(inputElem, baseID, "input");
    labelElem.setHtmlFor(inputElem.getId());
  }

  /**
   * This method is called when a widget is attached to the browser's document.
   * onAttach needs special handling for the InputLabel case. Must still call
   * {@link Widget#onAttach()} to preserve the <code>onAttach</code> contract.
   */
  @Override
  protected void onLoad() {
    setEventListener(inputElem, this);
  }

  /**
   * This method is called when a widget is detached from the browser's
   * document. Overridden because of IE bug that throws away checked state and
   * in order to clear the event listener off of the <code>inputElem</code>.
   */
  @Override
  protected void onUnload() {
    // Clear out the inputElem's event listener (breaking the circular
    // reference between it and the widget).
    setEventListener(asOld(inputElem), null);
    setValue(getValue());
  }

  /**
   * Replace the current input element with a new one. Preserves
   * all state except for the name property, for nasty reasons
   * related to radio button grouping. (See implementation of
   * {@link RadioButton#setName}.)
   *
   * @param elem the new input element
   */
  protected void replaceInputElement(Element elem) {
    InputElement newInputElem = InputElement.as(elem);
    // Collect information we need to set
    int tabIndex = getTabIndex();
    String checked = getValue();
    boolean enabled = isEnabled();
    String formValue = getFormValue();
    String uid = inputElem.getId();
    String accessKey = inputElem.getAccessKey();
    int sunkEvents = Event.getEventsSunk(inputElem);

    // Clear out the old input element
    setEventListener(asOld(inputElem), null);

    getElement().replaceChild(newInputElem, inputElem);

    // Sink events on the new element
    Event.sinkEvents(elem, Event.getEventsSunk(inputElem));
    Event.sinkEvents(inputElem, 0);
    inputElem = newInputElem;

    // Setup the new element
    Event.sinkEvents(inputElem, sunkEvents);
    inputElem.setId(uid);
    if (!accessKey.equals("")) {
      inputElem.setAccessKey(accessKey);
    }
    setTabIndex(tabIndex);
    setValue(checked);
    setEnabled(enabled);
    setFormValue(formValue);

    // Set the event listener
    if (isAttached()) {
      setEventListener(asOld(inputElem), this);
    }
  }

  private Element asOld(com.google.gwt.dom.client.Element elem) {
    Element oldSchool = elem.cast();
    return oldSchool;
  }

  private void setEventListener(com.google.gwt.dom.client.Element e,
                                EventListener listener) {
    DOM.setEventListener(asOld(e), listener);
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }
}