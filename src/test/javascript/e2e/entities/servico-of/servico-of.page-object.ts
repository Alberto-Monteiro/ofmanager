import { element, by, ElementFinder } from 'protractor';

export class ServicoOfComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('of-servico-of div table .btn-danger'));
  title = element.all(by.css('of-servico-of div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ServicoOfUpdatePage {
  pageTitle = element(by.id('of-servico-of-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  numeroInput = element(by.id('field_numero'));
  estadoSelect = element(by.id('field_estado'));
  observacaoDoGestorInput = element(by.id('field_observacaoDoGestor'));
  createdByInput = element(by.id('field_createdBy'));
  createdDateInput = element(by.id('field_createdDate'));
  lastModifiedByInput = element(by.id('field_lastModifiedBy'));
  lastModifiedDateInput = element(by.id('field_lastModifiedDate'));
  valorUstibbInput = element(by.id('field_valorUstibb'));

  gestorDaOfSelect = element(by.id('field_gestorDaOf'));
  donoDaOfSelect = element(by.id('field_donoDaOf'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNumeroInput(numero: string): Promise<void> {
    await this.numeroInput.sendKeys(numero);
  }

  async getNumeroInput(): Promise<string> {
    return await this.numeroInput.getAttribute('value');
  }

  async setEstadoSelect(estado: string): Promise<void> {
    await this.estadoSelect.sendKeys(estado);
  }

  async getEstadoSelect(): Promise<string> {
    return await this.estadoSelect.element(by.css('option:checked')).getText();
  }

  async estadoSelectLastOption(): Promise<void> {
    await this.estadoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setObservacaoDoGestorInput(observacaoDoGestor: string): Promise<void> {
    await this.observacaoDoGestorInput.sendKeys(observacaoDoGestor);
  }

  async getObservacaoDoGestorInput(): Promise<string> {
    return await this.observacaoDoGestorInput.getAttribute('value');
  }

  async setCreatedByInput(createdBy: string): Promise<void> {
    await this.createdByInput.sendKeys(createdBy);
  }

  async getCreatedByInput(): Promise<string> {
    return await this.createdByInput.getAttribute('value');
  }

  async setCreatedDateInput(createdDate: string): Promise<void> {
    await this.createdDateInput.sendKeys(createdDate);
  }

  async getCreatedDateInput(): Promise<string> {
    return await this.createdDateInput.getAttribute('value');
  }

  async setLastModifiedByInput(lastModifiedBy: string): Promise<void> {
    await this.lastModifiedByInput.sendKeys(lastModifiedBy);
  }

  async getLastModifiedByInput(): Promise<string> {
    return await this.lastModifiedByInput.getAttribute('value');
  }

  async setLastModifiedDateInput(lastModifiedDate: string): Promise<void> {
    await this.lastModifiedDateInput.sendKeys(lastModifiedDate);
  }

  async getLastModifiedDateInput(): Promise<string> {
    return await this.lastModifiedDateInput.getAttribute('value');
  }

  async setValorUstibbInput(valorUstibb: string): Promise<void> {
    await this.valorUstibbInput.sendKeys(valorUstibb);
  }

  async getValorUstibbInput(): Promise<string> {
    return await this.valorUstibbInput.getAttribute('value');
  }

  async gestorDaOfSelectLastOption(): Promise<void> {
    await this.gestorDaOfSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async gestorDaOfSelectOption(option: string): Promise<void> {
    await this.gestorDaOfSelect.sendKeys(option);
  }

  getGestorDaOfSelect(): ElementFinder {
    return this.gestorDaOfSelect;
  }

  async getGestorDaOfSelectedOption(): Promise<string> {
    return await this.gestorDaOfSelect.element(by.css('option:checked')).getText();
  }

  async donoDaOfSelectLastOption(): Promise<void> {
    await this.donoDaOfSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async donoDaOfSelectOption(option: string): Promise<void> {
    await this.donoDaOfSelect.sendKeys(option);
  }

  getDonoDaOfSelect(): ElementFinder {
    return this.donoDaOfSelect;
  }

  async getDonoDaOfSelectedOption(): Promise<string> {
    return await this.donoDaOfSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ServicoOfDeleteDialog {
  private dialogTitle = element(by.id('of-delete-servicoOf-heading'));
  private confirmButton = element(by.id('of-confirm-delete-servicoOf'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
