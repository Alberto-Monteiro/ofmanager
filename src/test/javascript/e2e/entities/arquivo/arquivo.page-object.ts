import { element, by, ElementFinder } from 'protractor';

export class ArquivoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('of-arquivo div table .btn-danger'));
  title = element.all(by.css('of-arquivo div h2#page-heading span')).first();
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

export class ArquivoUpdatePage {
  pageTitle = element(by.id('of-arquivo-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  caminhoDoArquivoInput = element(by.id('field_caminhoDoArquivo'));
  extensaoInput = element(by.id('field_extensao'));
  complexidadeSelect = element(by.id('field_complexidade'));
  arquivoDeTestInput = element(by.id('field_arquivoDeTest'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCaminhoDoArquivoInput(caminhoDoArquivo: string): Promise<void> {
    await this.caminhoDoArquivoInput.sendKeys(caminhoDoArquivo);
  }

  async getCaminhoDoArquivoInput(): Promise<string> {
    return await this.caminhoDoArquivoInput.getAttribute('value');
  }

  async setExtensaoInput(extensao: string): Promise<void> {
    await this.extensaoInput.sendKeys(extensao);
  }

  async getExtensaoInput(): Promise<string> {
    return await this.extensaoInput.getAttribute('value');
  }

  async setComplexidadeSelect(complexidade: string): Promise<void> {
    await this.complexidadeSelect.sendKeys(complexidade);
  }

  async getComplexidadeSelect(): Promise<string> {
    return await this.complexidadeSelect.element(by.css('option:checked')).getText();
  }

  async complexidadeSelectLastOption(): Promise<void> {
    await this.complexidadeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  getArquivoDeTestInput(): ElementFinder {
    return this.arquivoDeTestInput;
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

export class ArquivoDeleteDialog {
  private dialogTitle = element(by.id('of-delete-arquivo-heading'));
  private confirmButton = element(by.id('of-confirm-delete-arquivo'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
