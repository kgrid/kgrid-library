import { mount } from '@vue/test-utils'
import objcreator from '@/components/objcreator.vue'

describe('hello', () => {
  let wrapper;
  beforeEach(() => {
    wrapper = mount(objcreator);
  })

  it('has the expected html structure', () => {
    wrapper.vm.files=[{"name":"/hello-world-v0.0.1.zip"}];
    expect(wrapper.vm.arkid.toString()).toBe("hello/world/v0.0.1");
  })
})
