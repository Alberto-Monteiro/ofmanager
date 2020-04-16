import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ArtefatoOrdemDeFornecimentoComponentsPage,
  ArtefatoOrdemDeFornecimentoDeleteDialog,
  ArtefatoOrdemDeFornecimentoUpdatePage
} from './artefato-ordem-de-fornecimento.page-object';

const expect = chai.expect;

describe('ArtefatoOrdemDeFornecimento e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let artefatoOrdemDeFornecimentoComponentsPage: ArtefatoOrdemDeFornecimentoComponentsPage;
  let artefatoOrdemDeFornecimentoUpdatePage: ArtefatoOrdemDeFornecimentoUpdatePage;
  let artefatoOrdemDeFornecimentoDeleteDialog: ArtefatoOrdemDeFornecimentoDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ArtefatoOrdemDeFornecimentos', async () => {
    await navBarPage.goToEntity('artefato-ordem-de-fornecimento');
    artefatoOrdemDeFornecimentoComponentsPage = new ArtefatoOrdemDeFornecimentoComponentsPage();
    await browser.wait(ec.visibilityOf(artefatoOrdemDeFornecimentoComponentsPage.title), 5000);
    expect(await artefatoOrdemDeFornecimentoComponentsPage.getTitle()).to.eq('ofmanagerApp.artefatoOrdemDeFornecimento.home.title');
    await browser.wait(
      ec.or(
        ec.visibilityOf(artefatoOrdemDeFornecimentoComponentsPage.entities),
        ec.visibilityOf(artefatoOrdemDeFornecimentoComponentsPage.noResult)
      ),
      1000
    );
  });

  it('should load create ArtefatoOrdemDeFornecimento page', async () => {
    await artefatoOrdemDeFornecimentoComponentsPage.clickOnCreateButton();
    artefatoOrdemDeFornecimentoUpdatePage = new ArtefatoOrdemDeFornecimentoUpdatePage();
    expect(await artefatoOrdemDeFornecimentoUpdatePage.getPageTitle()).to.eq(
      'ofmanagerApp.artefatoOrdemDeFornecimento.home.createOrEditLabel'
    );
    await artefatoOrdemDeFornecimentoUpdatePage.cancel();
  });

  it('should create and save ArtefatoOrdemDeFornecimentos', async () => {
    const nbButtonsBeforeCreate = await artefatoOrdemDeFornecimentoComponentsPage.countDeleteButtons();

    await artefatoOrdemDeFornecimentoComponentsPage.clickOnCreateButton();

    await promise.all([
      artefatoOrdemDeFornecimentoUpdatePage.estadoSelectLastOption(),
      artefatoOrdemDeFornecimentoUpdatePage.setCreatedDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      artefatoOrdemDeFornecimentoUpdatePage.artefatoSelectLastOption(),
      artefatoOrdemDeFornecimentoUpdatePage.ordemDeFornecimentoSelectLastOption()
    ]);

    expect(await artefatoOrdemDeFornecimentoUpdatePage.getCreatedDateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected createdDate value to be equals to 2000-12-31'
    );

    await artefatoOrdemDeFornecimentoUpdatePage.save();
    expect(await artefatoOrdemDeFornecimentoUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await artefatoOrdemDeFornecimentoComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last ArtefatoOrdemDeFornecimento', async () => {
    const nbButtonsBeforeDelete = await artefatoOrdemDeFornecimentoComponentsPage.countDeleteButtons();
    await artefatoOrdemDeFornecimentoComponentsPage.clickOnLastDeleteButton();

    artefatoOrdemDeFornecimentoDeleteDialog = new ArtefatoOrdemDeFornecimentoDeleteDialog();
    expect(await artefatoOrdemDeFornecimentoDeleteDialog.getDialogTitle()).to.eq(
      'ofmanagerApp.artefatoOrdemDeFornecimento.delete.question'
    );
    await artefatoOrdemDeFornecimentoDeleteDialog.clickOnConfirmButton();

    expect(await artefatoOrdemDeFornecimentoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
