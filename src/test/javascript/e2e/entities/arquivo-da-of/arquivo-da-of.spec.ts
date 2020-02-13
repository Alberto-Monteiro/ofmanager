import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ArquivoDaOfComponentsPage, ArquivoDaOfDeleteDialog, ArquivoDaOfUpdatePage } from './arquivo-da-of.page-object';

const expect = chai.expect;

describe('ArquivoDaOf e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let arquivoDaOfComponentsPage: ArquivoDaOfComponentsPage;
  let arquivoDaOfUpdatePage: ArquivoDaOfUpdatePage;
  let arquivoDaOfDeleteDialog: ArquivoDaOfDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ArquivoDaOfs', async () => {
    await navBarPage.goToEntity('arquivo-da-of');
    arquivoDaOfComponentsPage = new ArquivoDaOfComponentsPage();
    await browser.wait(ec.visibilityOf(arquivoDaOfComponentsPage.title), 5000);
    expect(await arquivoDaOfComponentsPage.getTitle()).to.eq('ofmanagerApp.arquivoDaOf.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(arquivoDaOfComponentsPage.entities), ec.visibilityOf(arquivoDaOfComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ArquivoDaOf page', async () => {
    await arquivoDaOfComponentsPage.clickOnCreateButton();
    arquivoDaOfUpdatePage = new ArquivoDaOfUpdatePage();
    expect(await arquivoDaOfUpdatePage.getPageTitle()).to.eq('ofmanagerApp.arquivoDaOf.home.createOrEditLabel');
    await arquivoDaOfUpdatePage.cancel();
  });

  it('should create and save ArquivoDaOfs', async () => {
    const nbButtonsBeforeCreate = await arquivoDaOfComponentsPage.countDeleteButtons();

    await arquivoDaOfComponentsPage.clickOnCreateButton();

    await promise.all([
      arquivoDaOfUpdatePage.estadoArquivoSelectLastOption(),
      arquivoDaOfUpdatePage.servicoOfSelectLastOption(),
      arquivoDaOfUpdatePage.arquivoSelectLastOption()
    ]);

    await arquivoDaOfUpdatePage.save();
    expect(await arquivoDaOfUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await arquivoDaOfComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ArquivoDaOf', async () => {
    const nbButtonsBeforeDelete = await arquivoDaOfComponentsPage.countDeleteButtons();
    await arquivoDaOfComponentsPage.clickOnLastDeleteButton();

    arquivoDaOfDeleteDialog = new ArquivoDaOfDeleteDialog();
    expect(await arquivoDaOfDeleteDialog.getDialogTitle()).to.eq('ofmanagerApp.arquivoDaOf.delete.question');
    await arquivoDaOfDeleteDialog.clickOnConfirmButton();

    expect(await arquivoDaOfComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
