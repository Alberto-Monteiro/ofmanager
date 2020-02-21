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

  useridInput = element(by.id('field_userid'));
  numeroInput = element(by.id('field_numero'));
  createdDateInput = element(by.id('field_createdDate'));

  gestorDaOfSelect = element(by.id('field_gestorDaOf'));
  donoDaOfSelect = element(by.id('field_donoDaOf'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setUseridInput(userid: string): Promise<void> {
    await this.useridInput.sendKeys(userid);
  }

  async getUseridInput(): Promise<string> {
    return await this.useridInput.getAttribute('value');
  }

  async setNumeroInput(numero: string): Promise<void> {
    await this.numeroInput.sendKeys(numero);
  }

  async getNumeroInput(): Promise<string> {
    return await this.numeroInput.getAttribute('value');
  }

  async setCreatedDateInput(createdDate: string): Promise<void> {
    await this.createdDateInput.sendKeys(createdDate);
  }

  async getCreatedDateInput(): Promise<string> {
    return await this.createdDateInput.getAttribute('value');
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
