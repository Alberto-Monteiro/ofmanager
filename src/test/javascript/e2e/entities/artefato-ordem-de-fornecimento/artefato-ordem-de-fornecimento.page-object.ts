import { element, by, ElementFinder } from 'protractor';

export class ArtefatoOrdemDeFornecimentoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('of-artefato-ordem-de-fornecimento div table .btn-danger'));
  title = element.all(by.css('of-artefato-ordem-de-fornecimento div h2#page-heading span')).first();
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

export class ArtefatoOrdemDeFornecimentoUpdatePage {
  pageTitle = element(by.id('of-artefato-ordem-de-fornecimento-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  estadoSelect = element(by.id('field_estado'));
  createdDateInput = element(by.id('field_createdDate'));

  artefatoSelect = element(by.id('field_artefato'));
  ordemDeFornecimentoSelect = element(by.id('field_ordemDeFornecimento'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
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

  async setCreatedDateInput(createdDate: string): Promise<void> {
    await this.createdDateInput.sendKeys(createdDate);
  }

  async getCreatedDateInput(): Promise<string> {
    return await this.createdDateInput.getAttribute('value');
  }

  async artefatoSelectLastOption(): Promise<void> {
    await this.artefatoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async artefatoSelectOption(option: string): Promise<void> {
    await this.artefatoSelect.sendKeys(option);
  }

  getArtefatoSelect(): ElementFinder {
    return this.artefatoSelect;
  }

  async getArtefatoSelectedOption(): Promise<string> {
    return await this.artefatoSelect.element(by.css('option:checked')).getText();
  }

  async ordemDeFornecimentoSelectLastOption(): Promise<void> {
    await this.ordemDeFornecimentoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ordemDeFornecimentoSelectOption(option: string): Promise<void> {
    await this.ordemDeFornecimentoSelect.sendKeys(option);
  }

  getOrdemDeFornecimentoSelect(): ElementFinder {
    return this.ordemDeFornecimentoSelect;
  }

  async getOrdemDeFornecimentoSelectedOption(): Promise<string> {
    return await this.ordemDeFornecimentoSelect.element(by.css('option:checked')).getText();
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

export class ArtefatoOrdemDeFornecimentoDeleteDialog {
  private dialogTitle = element(by.id('of-delete-artefatoOrdemDeFornecimento-heading'));
  private confirmButton = element(by.id('of-confirm-delete-artefatoOrdemDeFornecimento'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
