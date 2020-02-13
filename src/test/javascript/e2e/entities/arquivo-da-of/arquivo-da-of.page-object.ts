import { element, by, ElementFinder } from 'protractor';

export class ArquivoDaOfComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('of-arquivo-da-of div table .btn-danger'));
  title = element.all(by.css('of-arquivo-da-of div h2#page-heading span')).first();
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

export class ArquivoDaOfUpdatePage {
  pageTitle = element(by.id('of-arquivo-da-of-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  estadoArquivoSelect = element(by.id('field_estadoArquivo'));

  servicoOfSelect = element(by.id('field_servicoOf'));
  arquivoSelect = element(by.id('field_arquivo'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setEstadoArquivoSelect(estadoArquivo: string): Promise<void> {
    await this.estadoArquivoSelect.sendKeys(estadoArquivo);
  }

  async getEstadoArquivoSelect(): Promise<string> {
    return await this.estadoArquivoSelect.element(by.css('option:checked')).getText();
  }

  async estadoArquivoSelectLastOption(): Promise<void> {
    await this.estadoArquivoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async servicoOfSelectLastOption(): Promise<void> {
    await this.servicoOfSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async servicoOfSelectOption(option: string): Promise<void> {
    await this.servicoOfSelect.sendKeys(option);
  }

  getServicoOfSelect(): ElementFinder {
    return this.servicoOfSelect;
  }

  async getServicoOfSelectedOption(): Promise<string> {
    return await this.servicoOfSelect.element(by.css('option:checked')).getText();
  }

  async arquivoSelectLastOption(): Promise<void> {
    await this.arquivoSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async arquivoSelectOption(option: string): Promise<void> {
    await this.arquivoSelect.sendKeys(option);
  }

  getArquivoSelect(): ElementFinder {
    return this.arquivoSelect;
  }

  async getArquivoSelectedOption(): Promise<string> {
    return await this.arquivoSelect.element(by.css('option:checked')).getText();
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

export class ArquivoDaOfDeleteDialog {
  private dialogTitle = element(by.id('of-delete-arquivoDaOf-heading'));
  private confirmButton = element(by.id('of-confirm-delete-arquivoDaOf'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
