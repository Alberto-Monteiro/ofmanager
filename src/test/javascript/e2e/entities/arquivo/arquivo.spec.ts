import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ArquivoComponentsPage, ArquivoDeleteDialog, ArquivoUpdatePage } from './arquivo.page-object';

const expect = chai.expect;

describe('Arquivo e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let arquivoComponentsPage: ArquivoComponentsPage;
  let arquivoUpdatePage: ArquivoUpdatePage;
  let arquivoDeleteDialog: ArquivoDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Arquivos', async () => {
    await navBarPage.goToEntity('arquivo');
    arquivoComponentsPage = new ArquivoComponentsPage();
    await browser.wait(ec.visibilityOf(arquivoComponentsPage.title), 5000);
    expect(await arquivoComponentsPage.getTitle()).to.eq('ofmanagerApp.arquivo.home.title');
    await browser.wait(ec.or(ec.visibilityOf(arquivoComponentsPage.entities), ec.visibilityOf(arquivoComponentsPage.noResult)), 1000);
  });

  it('should load create Arquivo page', async () => {
    await arquivoComponentsPage.clickOnCreateButton();
    arquivoUpdatePage = new ArquivoUpdatePage();
    expect(await arquivoUpdatePage.getPageTitle()).to.eq('ofmanagerApp.arquivo.home.createOrEditLabel');
    await arquivoUpdatePage.cancel();
  });

  it('should create and save Arquivos', async () => {
    const nbButtonsBeforeCreate = await arquivoComponentsPage.countDeleteButtons();

    await arquivoComponentsPage.clickOnCreateButton();

    await promise.all([
      arquivoUpdatePage.setCaminhoDoArquivoInput('caminhoDoArquivo'),
      arquivoUpdatePage.setExtensaoInput('extensao'),
      arquivoUpdatePage.complexidadeSelectLastOption()
    ]);

    expect(await arquivoUpdatePage.getCaminhoDoArquivoInput()).to.eq(
      'caminhoDoArquivo',
      'Expected CaminhoDoArquivo value to be equals to caminhoDoArquivo'
    );
    expect(await arquivoUpdatePage.getExtensaoInput()).to.eq('extensao', 'Expected Extensao value to be equals to extensao');

    await arquivoUpdatePage.save();
    expect(await arquivoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await arquivoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Arquivo', async () => {
    const nbButtonsBeforeDelete = await arquivoComponentsPage.countDeleteButtons();
    await arquivoComponentsPage.clickOnLastDeleteButton();

    arquivoDeleteDialog = new ArquivoDeleteDialog();
    expect(await arquivoDeleteDialog.getDialogTitle()).to.eq('ofmanagerApp.arquivo.delete.question');
    await arquivoDeleteDialog.clickOnConfirmButton();

    expect(await arquivoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
